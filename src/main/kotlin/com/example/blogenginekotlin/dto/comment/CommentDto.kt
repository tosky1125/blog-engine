package com.example.blogenginekotlin.dto.comment

import com.example.blogenginekotlin.dto.user.UserDto
import java.time.LocalDateTime

data class CommentDto(
    val id: Long,
    val content: String,
    val author: UserDto,
    val postId: Long,
    val parentId: Long? = null,
    val replies: List<CommentDto> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)