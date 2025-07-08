package com.example.blogenginekotlin.dto.post

import com.example.blogenginekotlin.dto.tag.TagDto
import com.example.blogenginekotlin.dto.user.UserDto
import java.time.LocalDateTime
import java.util.UUID

data class PostDto(
    val id: UUID,
    val title: String,
    val content: String,
    val excerpt: String? = null,
    val slug: String,
    val isPublished: Boolean,
    val publishedAt: LocalDateTime? = null,
    val author: UserDto,
    val tags: List<TagDto> = emptyList(),
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)