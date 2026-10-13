package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "TopicRequest", description = "Payload to create a topic inside a subject")
public record TopicRequest(

    @Schema(example = "JPA and Hibernate mapping")
    @NotBlank(message = "name is required")
    String name,

    @Schema(example = "1")
    @NotNull(message = "subjectId is required")
    Long subjectId

) {}
