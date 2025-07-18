package com.example.blogenginekotlin.dto.post

import com.example.blogenginekotlin.dto.tag.TagDto
import com.example.blogenginekotlin.dto.user.UserDto
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val title: String,
    val content: String,
    val summary: String,
    val coverImageUrl: String? = null,
    val published: Boolean,
    val publishedAt: LocalDateTime? = null,
    val author: UserDto,
    val tags: List<TagDto> = emptyList(),
    val viewCount: Long = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)