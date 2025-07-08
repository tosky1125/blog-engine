package com.example.blogenginekotlin.dto.post

import jakarta.validation.constraints.Size
import java.util.UUID

data class UpdatePostRequest(
    @field:Size(max = 255, message = "Title must not exceed 255 characters")
    val title: String? = null,

    val content: String? = null,

    @field:Size(max = 500, message = "Excerpt must not exceed 500 characters")
    val excerpt: String? = null,

    val isPublished: Boolean? = null,

    val tagIds: Set<UUID>? = null
)