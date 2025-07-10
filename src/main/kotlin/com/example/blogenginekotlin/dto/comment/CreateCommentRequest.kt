package com.example.blogenginekotlin.dto.comment

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCommentRequest(
    @field:NotBlank(message = "Content is required")
    @field:Size(max = 1000, message = "Content must not exceed 1000 characters")
    val content: String,

    val postId: Long,

    val parentId: Long? = null
)