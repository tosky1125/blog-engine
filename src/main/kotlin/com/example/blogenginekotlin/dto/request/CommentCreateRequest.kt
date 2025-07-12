package com.example.blogenginekotlin.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentCreateRequest(
    @field:NotBlank(message = "Content is required")
    @field:Size(max = 1000, message = "Comment must not exceed 1000 characters")
    val content: String
)