package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.Post
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val title: String,
    val content: String,
    val summary: String?,
    val coverImageUrl: String?,
    val authorId: Long,
    val authorEmail: String,
    val authorName: String,
    val isPublished: Boolean,
    val publishedAt: LocalDateTime?,
    val viewCount: Long,
    val likeCount: Int,
    val commentCount: Int,
    val tags: List<TagDto>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostDto {
            return PostDto(
                id = post.id!!,
                title = post.title,
                content = post.content,
                summary = post.summary,
                coverImageUrl = post.coverImageUrl,
                authorId = post.author.id!!,
                authorEmail = post.author.email,
                authorName = post.author.name,
                isPublished = post.published,
                publishedAt = post.publishedAt,
                viewCount = post.viewCount,
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                tags = post.tags.map { TagDto.from(it) },
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }
    }
}