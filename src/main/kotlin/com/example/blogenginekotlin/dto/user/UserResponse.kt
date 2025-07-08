package com.example.blogenginekotlin.dto.user

import com.example.blogenginekotlin.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val name: String,
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                name = user.name,
                bio = user.bio,
                profileImageUrl = user.profileImageUrl,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}