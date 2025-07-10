package com.example.blogenginekotlin.dto.post

import jakarta.validation.constraints.Size

data class UpdatePostRequest(
    @field:Size(max = 255, message = "Title must not exceed 255 characters")
    val title: String? = null,

    @field:Size(max = 1000, message = "Summary must not exceed 1000 characters")
    val summary: String? = null,

    val content: String? = null,

    val coverImageUrl: String? = null,

    val published: Boolean? = null,

    val tagIds: Set<Long>? = null
)