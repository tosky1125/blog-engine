package com.example.blogenginekotlin.dto.tag

import java.time.LocalDateTime

data class TagDto(
    val id: Long,
    val name: String,
    val slug: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)