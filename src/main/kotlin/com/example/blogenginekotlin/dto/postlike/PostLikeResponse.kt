package com.example.blogenginekotlin.dto.postlike

import com.example.blogenginekotlin.entity.PostLike
import java.time.LocalDateTime

data class PostLikeResponse(
    val id: Long,
    val userId: Long,
    val postId: Long,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(postLike: PostLike): PostLikeResponse {
            return PostLikeResponse(
                id = postLike.id,
                userId = postLike.user.id,
                postId = postLike.post.id,
                createdAt = postLike.createdAt
            )
        }
    }
}