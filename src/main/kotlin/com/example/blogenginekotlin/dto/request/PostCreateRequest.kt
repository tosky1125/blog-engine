package com.example.blogenginekotlin.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PostCreateRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must not exceed 200 characters")
    val title: String,

    @field:NotBlank(message = "Content is required")
    val content: String,

    @field:Size(max = 500, message = "Summary must not exceed 500 characters")
    val summary: String?,

    val coverImageUrl: String?,

    val tagNames: List<String>?,

    val isPublished: Boolean?
)