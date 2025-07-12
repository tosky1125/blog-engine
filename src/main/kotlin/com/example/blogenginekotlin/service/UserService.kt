package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.request.UserCreateRequest
import com.example.blogenginekotlin.dto.request.UserUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {
    fun createUser(request: UserCreateRequest): UserDto
    fun getUserById(id: Long): UserDto
    fun getUserByUsername(username: String): UserDto
    fun getUserByEmail(email: String): UserDto
    fun updateUser(id: Long, request: UserUpdateRequest): UserDto
    fun deleteUser(id: Long)
    fun getAllUsers(pageable: Pageable): Page<UserDto>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}