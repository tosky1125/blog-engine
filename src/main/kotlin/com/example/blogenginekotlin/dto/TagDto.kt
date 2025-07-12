package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.Tag
import java.time.LocalDateTime

data class TagDto(
    val id: Long,
    val name: String,
    val slug: String,
    val postCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(tag: Tag): TagDto {
            return TagDto(
                id = tag.id!!,
                name = tag.name,
                slug = tag.slug,
                postCount = tag.posts.size,
                createdAt = tag.createdAt,
                updatedAt = tag.updatedAt
            )
        }
    }
}