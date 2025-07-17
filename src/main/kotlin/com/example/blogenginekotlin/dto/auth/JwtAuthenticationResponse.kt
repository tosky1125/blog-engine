package com.example.blogenginekotlin.dto.auth

import com.example.blogenginekotlin.dto.UserDto

data class JwtAuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserDto
)