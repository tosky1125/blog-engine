package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.ApiResponse
import com.example.blogenginekotlin.dto.auth.JwtAuthenticationResponse
import com.example.blogenginekotlin.dto.auth.LoginRequest
import com.example.blogenginekotlin.dto.auth.RefreshTokenRequest
import com.example.blogenginekotlin.dto.auth.RegisterRequest
import com.example.blogenginekotlin.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<JwtAuthenticationResponse>> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                message = "User registered successfully",
                data = response
            )
        )
    }
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<JwtAuthenticationResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Login successful",
                data = response
            )
        )
    }
    
    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResponse<JwtAuthenticationResponse>> {
        val response = authService.refreshToken(request.refreshToken)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Token refreshed successfully",
                data = response
            )
        )
    }
}