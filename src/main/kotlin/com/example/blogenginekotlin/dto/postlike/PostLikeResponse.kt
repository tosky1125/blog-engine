package com.example.blogenginekotlin.dto.postlike

import com.example.blogenginekotlin.entity.PostLike
import java.time.LocalDateTime
import java.util.UUID

data class PostLikeResponse(
    val id: UUID,
    val userId: UUID,
    val postId: UUID,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(postLike: PostLike): PostLikeResponse {
            return PostLikeResponse(
                id = postLike.id!!,
                userId = postLike.user.id!!,
                postId = postLike.post.id!!,
                createdAt = postLike.createdAt
            )
        }
    }
}