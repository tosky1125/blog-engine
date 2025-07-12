package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.request.UserCreateRequest
import com.example.blogenginekotlin.dto.request.UserUpdateRequest
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @Transactional
    override fun createUser(request: UserCreateRequest): UserDto {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username ${request.username} already exists")
        }
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email ${request.email} already exists")
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            bio = request.bio,
            avatarUrl = request.avatarUrl
        )

        val savedUser = userRepository.save(user)
        return UserDto.from(savedUser)
    }

    override fun getUserById(id: Long): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with id: $id") }
        return UserDto.from(user)
    }

    override fun getUserByUsername(username: String): UserDto {
        val user = userRepository.findByUsername(username)
            ?: throw NoSuchElementException("User not found with username: $username")
        return UserDto.from(user)
    }

    override fun getUserByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(email)
            ?: throw NoSuchElementException("User not found with email: $email")
        return UserDto.from(user)
    }

    @Transactional
    override fun updateUser(id: Long, request: UserUpdateRequest): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with id: $id") }

        request.username?.let {
            if (it != user.username && userRepository.existsByUsername(it)) {
                throw IllegalArgumentException("Username $it already exists")
            }
            user.username = it
        }

        request.email?.let {
            if (it != user.email && userRepository.existsByEmail(it)) {
                throw IllegalArgumentException("Email $it already exists")
            }
            user.email = it
        }

        request.password?.let {
            user.setPassword(passwordEncoder.encode(it))
        }

        request.name?.let { user.name = it }
        request.bio?.let { user.bio = it }
        request.avatarUrl?.let { user.avatarUrl = it }

        val updatedUser = userRepository.save(user)
        return UserDto.from(updatedUser)
    }

    @Transactional
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw NoSuchElementException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }

    override fun getAllUsers(pageable: Pageable): Page<UserDto> {
        return userRepository.findAll(pageable).map { UserDto.from(it) }
    }

    override fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}