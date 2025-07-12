package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.Comment
import java.time.LocalDateTime

data class CommentDto(
    val id: Long,
    val content: String,
    val authorId: Long,
    val authorUsername: String,
    val postId: Long,
    val parentCommentId: Long?,
    val isEdited: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(comment: Comment): CommentDto {
            return CommentDto(
                id = comment.id!!,
                content = comment.content,
                authorId = comment.author.id!!,
                authorUsername = comment.author.username,
                postId = comment.post.id!!,
                parentCommentId = comment.parent?.id,
                isEdited = comment.isEdited,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}