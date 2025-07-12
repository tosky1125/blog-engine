package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.User
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val name: String?,
    val bio: String?,
    val avatarUrl: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
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