package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.Post
import java.time.LocalDateTime

data class PostDetailDto(
    val id: Long,
    val title: String,
    val content: String,
    val summary: String?,
    val coverImageUrl: String?,
    val author: UserDto,
    val isPublished: Boolean,
    val publishedAt: LocalDateTime?,
    val viewCount: Long,
    val likeCount: Long,
    val tags: List<TagDto>,
    val comments: List<CommentDto>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostDetailDto {
            return PostDetailDto(
                id = post.id!!,
                title = post.title,
                content = post.content,
                summary = post.summary,
                coverImageUrl = post.coverImageUrl,
                author = UserDto.from(post.author),
                isPublished = post.published,
                publishedAt = post.publishedAt,
                viewCount = post.viewCount,
                likeCount = post.likeCount.toLong(),
                tags = post.tags.map { TagDto.from(it) },
                comments = post.comments.map { CommentDto.from(it) },
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }
    }
}