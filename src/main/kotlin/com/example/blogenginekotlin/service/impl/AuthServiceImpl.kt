package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.auth.JwtAuthenticationResponse
import com.example.blogenginekotlin.dto.auth.LoginRequest
import com.example.blogenginekotlin.dto.auth.RegisterRequest
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.entity.UserRole
import com.example.blogenginekotlin.exception.DuplicateResourceException
import com.example.blogenginekotlin.exception.UnauthorizedException
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.security.jwt.JwtTokenProvider
import com.example.blogenginekotlin.service.AuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    @Value("\${jwt.expiration}")
    private val jwtExpirationInMs: Long
) : AuthService {
    
    @Transactional
    override fun register(request: RegisterRequest): JwtAuthenticationResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw DuplicateResourceException("Username '${request.username}' is already taken")
        }
        
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateResourceException("Email '${request.email}' is already registered")
        }
        
        val user = User(
            username = request.username,
            email = request.email,
            displayName = request.displayName,
            bio = request.bio,
            role = UserRole.USER,
            isActive = true
        )
        user.password = passwordEncoder.encode(request.password)
        
        val savedUser = userRepository.save(user)
        
        val accessToken = jwtTokenProvider.generateAccessToken(savedUser.username)
        val refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.username)
        
        return JwtAuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpirationInMs,
            user = UserDto.from(savedUser)
        )
    }
    
    @Transactional(readOnly = true)
    override fun login(request: LoginRequest): JwtAuthenticationResponse {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password
                )
            )
            
            val user = userRepository.findByUsername(request.username)
                ?: throw UnauthorizedException("Invalid credentials")
            
            val accessToken = jwtTokenProvider.generateAccessToken(authentication)
            val refreshToken = jwtTokenProvider.generateRefreshToken(user.username)
            
            return JwtAuthenticationResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = jwtExpirationInMs,
                user = UserDto.from(user)
            )
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Invalid credentials")
        }
    }
    
    @Transactional(readOnly = true)
    override fun refreshToken(refreshToken: String): JwtAuthenticationResponse {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw UnauthorizedException("Invalid refresh token")
        }
        
        val username = jwtTokenProvider.getUsernameFromToken(refreshToken)
        val user = userRepository.findByUsername(username)
            ?: throw UnauthorizedException("User not found")
        
        if (!user.isActive) {
            throw UnauthorizedException("User account is disabled")
        }
        
        val newAccessToken = jwtTokenProvider.generateAccessToken(username)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(username)
        
        return JwtAuthenticationResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiresIn = jwtExpirationInMs,
            user = UserDto.from(user)
        )
    }
}