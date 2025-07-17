package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.auth.JwtAuthenticationResponse
import com.example.blogenginekotlin.dto.auth.LoginRequest
import com.example.blogenginekotlin.dto.auth.RegisterRequest

interface AuthService {
    fun register(request: RegisterRequest): JwtAuthenticationResponse
    fun login(request: LoginRequest): JwtAuthenticationResponse
    fun refreshToken(refreshToken: String): JwtAuthenticationResponse
}