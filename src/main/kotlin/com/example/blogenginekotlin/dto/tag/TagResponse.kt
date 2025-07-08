package com.example.blogenginekotlin.dto.tag

import com.example.blogenginekotlin.entity.Tag
import java.time.LocalDateTime
import java.util.UUID

data class TagResponse(
    val id: UUID,
    val name: String,
    val slug: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(tag: Tag): TagResponse {
            return TagResponse(
                id = tag.id!!,
                name = tag.name,
                slug = tag.slug,
                createdAt = tag.createdAt,
                updatedAt = tag.updatedAt
            )
        }
    }
}