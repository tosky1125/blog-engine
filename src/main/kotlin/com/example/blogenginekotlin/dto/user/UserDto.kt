package com.example.blogenginekotlin.dto.user

import java.time.LocalDateTime
import java.util.UUID

data class UserDto(
    val id: UUID,
    val username: String,
    val email: String,
    val name: String,
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)