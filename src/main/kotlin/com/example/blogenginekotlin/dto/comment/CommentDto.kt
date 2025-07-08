package com.example.blogenginekotlin.dto.comment

import com.example.blogenginekotlin.dto.user.UserDto
import java.time.LocalDateTime
import java.util.UUID

data class CommentDto(
    val id: UUID,
    val content: String,
    val author: UserDto,
    val postId: UUID,
    val parentCommentId: UUID? = null,
    val replies: List<CommentDto> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)