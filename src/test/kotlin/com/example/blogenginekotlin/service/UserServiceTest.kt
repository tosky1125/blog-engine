package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.request.UserCreateRequest
import com.example.blogenginekotlin.dto.request.UserUpdateRequest
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.impl.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        passwordEncoder = mockk()
        userService = UserServiceImpl(userRepository, passwordEncoder)
    }

    @Test
    fun `createUser should create a new user successfully`() {
        // Given
        val request = UserCreateRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            name = "Test User",
            bio = "Test bio",
            avatarUrl = "http://example.com/avatar.jpg"
        )
        
        val encodedPassword = "encodedPassword123"
        val savedUser = mockk<User>(relaxed = true) {
            every { id } returns 1L
            every { username } returns request.username
            every { email } returns request.email
            every { name } returns request.name
            every { bio } returns request.bio
            every { avatarUrl } returns request.avatarUrl
            every { isActive } returns true
        }

        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns false
        every { passwordEncoder.encode(request.password) } returns encodedPassword
        every { userRepository.save(any()) } returns savedUser

        // When
        val result = userService.createUser(request)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals(savedUser.username, result.username)
        assertEquals(savedUser.email, result.email)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `createUser should throw exception when username already exists`() {
        // Given
        val request = UserCreateRequest(
            username = "existinguser",
            email = "test@example.com",
            password = "password123",
            name = null,
            bio = null,
            avatarUrl = null
        )

        every { userRepository.existsByUsername(request.username) } returns true

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            userService.createUser(request)
        }
        assertEquals("Username ${request.username} already exists", exception.message)
    }

    @Test
    fun `getUserById should return user when found`() {
        // Given
        val userId = 1L
        val user = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { username } returns "testuser"
            every { email } returns "test@example.com"
            every { isActive } returns true
        }

        every { userRepository.findById(userId) } returns Optional.of(user)

        // When
        val result = userService.getUserById(userId)

        // Then
        assertNotNull(result)
        assertEquals(userId, result.id)
        assertEquals("testuser", result.username)
    }

    @Test
    fun `getUserById should throw exception when user not found`() {
        // Given
        val userId = 999L
        every { userRepository.findById(userId) } returns Optional.empty()

        // When & Then
        val exception = assertThrows<NoSuchElementException> {
            userService.getUserById(userId)
        }
        assertEquals("User not found with id: $userId", exception.message)
    }

    @Test
    fun `updateUser should update user successfully`() {
        // Given
        val userId = 1L
        val existingUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { username } returns "oldusername"
            every { email } returns "old@example.com"
            every { isActive } returns true
        }

        val request = UserUpdateRequest(
            username = "newusername",
            email = "new@example.com",
            password = "newpassword",
            name = "New Name",
            bio = "New bio",
            avatarUrl = "http://example.com/new-avatar.jpg"
        )

        val encodedPassword = "encodedNewPassword"

        every { userRepository.findById(userId) } returns Optional.of(existingUser)
        every { userRepository.existsByUsername("newusername") } returns false
        every { userRepository.existsByEmail("new@example.com") } returns false
        every { passwordEncoder.encode(request.password) } returns encodedPassword
        every { userRepository.save(any()) } returns existingUser

        // When
        val result = userService.updateUser(userId, request)

        // Then
        assertNotNull(result)
        assertEquals(userId, result.id)
        verify { userRepository.save(existingUser) }
    }

    @Test
    fun `deleteUser should delete user when exists`() {
        // Given
        val userId = 1L
        every { userRepository.existsById(userId) } returns true
        every { userRepository.deleteById(userId) } returns Unit

        // When
        userService.deleteUser(userId)

        // Then
        verify { userRepository.deleteById(userId) }
    }

    @Test
    fun `getAllUsers should return paged users`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val user1 = mockk<User>(relaxed = true) {
            every { id } returns 1L
            every { username } returns "user1"
            every { email } returns "user1@example.com"
            every { isActive } returns true
        }
        val user2 = mockk<User>(relaxed = true) {
            every { id } returns 2L
            every { username } returns "user2"
            every { email } returns "user2@example.com"
            every { isActive } returns true
        }
        val users = listOf(user1, user2)
        val page = PageImpl(users, pageable, users.size.toLong())

        every { userRepository.findAll(pageable) } returns page

        // When
        val result = userService.getAllUsers(pageable)

        // Then
        assertEquals(2, result.content.size)
        assertEquals(users.size.toLong(), result.totalElements)
    }
}