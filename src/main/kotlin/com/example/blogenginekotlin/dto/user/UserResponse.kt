package com.example.blogenginekotlin.dto.user

import com.example.blogenginekotlin.entity.User
import java.time.LocalDateTime
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val name: String?,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                name = user.name,
                bio = user.bio,
                avatarUrl = user.avatarUrl,
                isActive = user.isActive,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}