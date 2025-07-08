package com.example.blogenginekotlin.dto.comment

import com.example.blogenginekotlin.dto.user.UserResponse
import com.example.blogenginekotlin.entity.Comment
import java.time.LocalDateTime
import java.util.UUID

data class CommentResponse(
    val id: UUID,
    val content: String,
    val author: UserResponse,
    val postId: UUID,
    val parentCommentId: UUID? = null,
    val replies: List<CommentResponse> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(comment: Comment, includeReplies: Boolean = false): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                content = comment.content,
                author = UserResponse.from(comment.author),
                postId = comment.post.id!!,
                parentCommentId = comment.parentComment?.id,
                replies = if (includeReplies) {
                    comment.replies.map { from(it, false) }
                } else {
                    emptyList()
                },
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}