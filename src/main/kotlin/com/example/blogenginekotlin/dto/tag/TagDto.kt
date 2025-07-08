package com.example.blogenginekotlin.dto.tag

import java.time.LocalDateTime
import java.util.UUID

data class TagDto(
    val id: UUID,
    val name: String,
    val slug: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)