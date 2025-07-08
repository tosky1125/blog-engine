package com.example.blogenginekotlin.dto.post

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreatePostRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 255, message = "Title must not exceed 255 characters")
    val title: String,

    @field:NotBlank(message = "Content is required")
    val content: String,

    @field:Size(max = 500, message = "Excerpt must not exceed 500 characters")
    val excerpt: String? = null,

    val isPublished: Boolean = false,

    val tagIds: Set<UUID> = emptySet()
)