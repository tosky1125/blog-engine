package com.example.blogenginekotlin.dto.post

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreatePostRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 255, message = "Title must not exceed 255 characters")
    val title: String,

    @field:NotBlank(message = "Summary is required")
    @field:Size(max = 1000, message = "Summary must not exceed 1000 characters")
    val summary: String,

    @field:NotBlank(message = "Content is required")
    val content: String,

    val coverImageUrl: String? = null,

    val published: Boolean = false,

    val tagIds: Set<Long> = emptySet()
)