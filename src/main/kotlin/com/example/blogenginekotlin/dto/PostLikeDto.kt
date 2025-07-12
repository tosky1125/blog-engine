package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.PostLike
import java.time.LocalDateTime

data class PostLikeDto(
    val id: Long,
    val userId: Long,
    val postId: Long,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(postLike: PostLike): PostLikeDto {
            return PostLikeDto(
                id = postLike.id!!,
                userId = postLike.user.id!!,
                postId = postLike.post.id!!,
                createdAt = postLike.createdAt
            )
        }
    }
}