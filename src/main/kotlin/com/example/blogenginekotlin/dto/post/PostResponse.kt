package com.example.blogenginekotlin.dto.post

import com.example.blogenginekotlin.dto.tag.TagResponse
import com.example.blogenginekotlin.dto.user.UserResponse
import com.example.blogenginekotlin.entity.Post
import java.time.LocalDateTime
import java.util.UUID

data class PostResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val excerpt: String? = null,
    val slug: String,
    val isPublished: Boolean,
    val publishedAt: LocalDateTime? = null,
    val author: UserResponse,
    val tags: List<TagResponse> = emptyList(),
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post, likeCount: Int = 0, commentCount: Int = 0): PostResponse {
            return PostResponse(
                id = post.id!!,
                title = post.title,
                content = post.content,
                excerpt = post.excerpt,
                slug = post.slug,
                isPublished = post.isPublished,
                publishedAt = post.publishedAt,
                author = UserResponse.from(post.author),
                tags = post.tags.map { TagResponse.from(it) },
                viewCount = post.viewCount,
                likeCount = likeCount,
                commentCount = commentCount,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }
    }
}