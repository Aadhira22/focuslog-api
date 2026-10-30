package com.example.focuslog_api.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * End-to-end tests that send real HTTP-style requests through the full Spring stack
 * (controller, validation, service, JPA, exception handler) against in-memory H2.
 * Each test registers its own user with a unique email, so tests do not depend on each other
 * or on the seeded demo data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    private static final String PASSWORD = "password123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- helpers ----------

    private String uniqueEmail() {
        return "user-" + UUID.randomUUID() + "@example.com";
    }

    private String json(Object body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    private JsonNode registerUser(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "Test User", "email", email, "password", PASSWORD))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private JsonNode createSubject(String name, long userId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", name, "userId", userId))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    // ---------- registration ----------

    @Test
    void register_returnsCreatedUser_withoutPassword() throws Exception {
        String email = uniqueEmail();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "Ada", "email", email, "password", PASSWORD))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void register_duplicateEmail_returnsConflict() throws Exception {
        String email = uniqueEmail();
        registerUser(email);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "Other", "email", email, "password", PASSWORD))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void register_invalidEmail_returnsBadRequestWithFieldMessage() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "Ada", "email", "not-an-email", "password", PASSWORD))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("email")));
    }

    // ---------- login ----------

    @Test
    void login_withValidCredentials_returnsJwt() throws Exception {
        String email = uniqueEmail();
        registerUser(email);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", email, "password", PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                // a JWT is three dot-separated parts: header.payload.signature
                .andExpect(jsonPath("$.token").value(org.hamcrest.Matchers.matchesPattern("^[\\w-]+\\.[\\w-]+\\.[\\w-]+$")));
    }

    @Test
    void login_withWrongPassword_returnsUnauthorized() throws Exception {
        String email = uniqueEmail();
        registerUser(email);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", email, "password", "wrong-password"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void login_withUnknownEmail_returnsSameErrorAsWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("email", uniqueEmail(), "password", PASSWORD))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void login_withMissingFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ---------- subjects ----------

    @Test
    void createSubject_thenFetchById_returnsSameSubject() throws Exception {
        long userId = registerUser(uniqueEmail()).get("id").asLong();
        JsonNode created = createSubject("Operating Systems", userId);

        mockMvc.perform(get("/api/subjects/{id}", created.get("id").asLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Operating Systems"))
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    void createSubject_withBlankName_returnsBadRequest() throws Exception {
        long userId = registerUser(uniqueEmail()).get("id").asLong();

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "", "userId", userId))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("name")));
    }

    @Test
    void getSubjectsByUser_returnsOnlyThatUsersSubjects() throws Exception {
        long userA = registerUser(uniqueEmail()).get("id").asLong();
        long userB = registerUser(uniqueEmail()).get("id").asLong();
        createSubject("Subject A1", userA);
        createSubject("Subject A2", userA);
        createSubject("Subject B1", userB);

        mockMvc.perform(get("/api/subjects/by-user/{userId}", userA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", not(org.hamcrest.Matchers.hasItem("Subject B1"))));
    }

    @Test
    void renameSubject_updatesName() throws Exception {
        long userId = registerUser(uniqueEmail()).get("id").asLong();
        long subjectId = createSubject("Old name", userId).get("id").asLong();

        mockMvc.perform(put("/api/subjects/{id}", subjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("name", "New name"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New name"));
    }

    @Test
    void deleteSubject_thenFetch_returnsNotFound() throws Exception {
        long userId = registerUser(uniqueEmail()).get("id").asLong();
        long subjectId = createSubject("Temporary", userId).get("id").asLong();

        mockMvc.perform(delete("/api/subjects/{id}", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subjectId));

        mockMvc.perform(get("/api/subjects/{id}", subjectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSubject_unknownId_returnsNotFoundJsonError() throws Exception {
        mockMvc.perform(get("/api/subjects/{id}", 999_999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("999999")));
    }

    // ---------- stats ----------

    @Test
    void stats_forNewUserWithOneSubject_returnEmptyProgress() throws Exception {
        long userId = registerUser(uniqueEmail()).get("id").asLong();
        createSubject("Databases", userId);

        mockMvc.perform(get("/api/stats/users/{userId}/accuracy-by-subject", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/stats/users/{userId}/weekly-study-time", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}