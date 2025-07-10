package com.example.blogenginekotlin.dto.post

import com.example.blogenginekotlin.dto.tag.TagResponse
import com.example.blogenginekotlin.dto.user.UserResponse
import com.example.blogenginekotlin.entity.Post
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val summary: String,
    val coverImageUrl: String? = null,
    val published: Boolean,
    val publishedAt: LocalDateTime? = null,
    val author: UserResponse,
    val tags: List<TagResponse> = emptyList(),
    val viewCount: Long = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post): PostResponse {
            return PostResponse(
                id = post.id,
                title = post.title,
                content = post.content,
                summary = post.summary,
                coverImageUrl = post.coverImageUrl,
                published = post.published,
                publishedAt = post.publishedAt,
                author = UserResponse.from(post.author),
                tags = post.tags.map { TagResponse.from(it) },
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }
    }
}