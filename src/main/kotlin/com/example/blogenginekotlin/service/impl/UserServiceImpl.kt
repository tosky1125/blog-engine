package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.user.CreateUserRequest
import com.example.blogenginekotlin.dto.user.UpdateUserRequest
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
    override fun createUser(request: CreateUserRequest): UserDto {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email ${request.email} already exists")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            bio = request.bio,
            profileImageUrl = request.profileImageUrl
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
        // In this system, email is used as username
        return getUserByEmail(username)
    }

    override fun getUserByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("User not found with email: $email") }
        return UserDto.from(user)
    }

    @Transactional
    override fun updateUser(id: Long, request: UpdateUserRequest): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with id: $id") }

        // User 엔티티의 필드들은 val로 선언되어 있어서 직접 수정할 수 없습니다.
        // 새로운 User 객체를 생성해야 합니다.
        var updatedEmail = user.email
        var updatedPassword = user.password
        var updatedName = user.name
        var updatedBio = user.bio
        var updatedProfileImageUrl = user.profileImageUrl

        request.email?.let {
            if (it != user.email && userRepository.existsByEmail(it)) {
                throw IllegalArgumentException("Email $it already exists")
            }
            updatedEmail = it
        }

        request.password?.let {
            updatedPassword = passwordEncoder.encode(it)
        }

        request.name?.let { updatedName = it }
        request.bio?.let { updatedBio = it }
        request.profileImageUrl?.let { updatedProfileImageUrl = it }

        // 변경사항이 있는 경우에만 업데이트
        if (updatedEmail != user.email || 
            updatedPassword != user.password || 
            updatedName != user.name || 
            updatedBio != user.bio || 
            updatedProfileImageUrl != user.profileImageUrl) {
            
            val updatedUser = User(
                email = updatedEmail,
                password = updatedPassword,
                name = updatedName,
                bio = updatedBio,
                profileImageUrl = updatedProfileImageUrl,
                role = user.role,
                enabled = user.enabled
            ).apply {
                id = user.id
                createdAt = user.createdAt
                updatedAt = user.updatedAt
            }
            
            return UserDto.from(userRepository.save(updatedUser))
        }

        return UserDto.from(user)
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
        // In this system, email is used as username
        return userRepository.existsByEmail(username)
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}