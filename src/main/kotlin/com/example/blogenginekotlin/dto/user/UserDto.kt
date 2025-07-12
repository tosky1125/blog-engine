package com.example.blogenginekotlin.dto.user

import java.time.LocalDateTime
data class UserDto(
    val id: Long,
    val email: String,
    val name: String,
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)