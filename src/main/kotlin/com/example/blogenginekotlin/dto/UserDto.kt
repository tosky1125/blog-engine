package com.example.blogenginekotlin.dto

import com.example.blogenginekotlin.entity.User
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val email: String,
    val name: String,
    val bio: String?,
    val profileImageUrl: String?,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!,
                email = user.email,
                name = user.name,
                bio = user.bio,
                profileImageUrl = user.profileImageUrl,
                enabled = user.enabled,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}