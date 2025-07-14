package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.request.UserCreateRequest
import com.example.blogenginekotlin.dto.request.UserUpdateRequest
import com.example.blogenginekotlin.exception.ResourceNotFoundException
import com.example.blogenginekotlin.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `should create user successfully`() {
        val request = UserCreateRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            name = "Test User",
            bio = "Test bio",
            avatarUrl = null
        )

        val userDto = UserDto(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            name = "Test User",
            bio = "Test bio",
            avatarUrl = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(userService.createUser(any())).thenReturn(userDto)

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `should get user by id successfully`() {
        val userDto = UserDto(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            name = "Test User",
            bio = "Test bio",
            avatarUrl = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(userService.getUserById(1L)).thenReturn(userDto)

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testuser"))
    }

    @Test
    fun `should return 404 when user not found`() {
        whenever(userService.getUserById(999L))
            .thenThrow(ResourceNotFoundException("User", "id", 999L))

        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all users with pagination`() {
        val users = listOf(
            UserDto(
                id = 1L,
                username = "user1",
                email = "user1@example.com",
                name = "User 1",
                bio = null,
                avatarUrl = null,
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            UserDto(
                id = 2L,
                username = "user2",
                email = "user2@example.com",
                name = "User 2",
                bio = null,
                avatarUrl = null,
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val pageable = PageRequest.of(0, 20)
        val page = PageImpl(users, pageable, 2)

        whenever(userService.getAllUsers(any())).thenReturn(page)

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].username").value("user1"))
            .andExpect(jsonPath("$.content[1].username").value("user2"))
    }

    @Test
    fun `should update user successfully`() {
        val request = UserUpdateRequest(
            username = "testuser",
            email = "test@example.com",
            password = null,
            name = "Updated Name",
            bio = "Updated bio",
            avatarUrl = "http://example.com/image.jpg"
        )

        val updatedUser = UserDto(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            name = "Updated Name",
            bio = "Updated bio",
            avatarUrl = "http://example.com/image.jpg",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(userService.updateUser(any(), any())).thenReturn(updatedUser)

        mockMvc.perform(
            put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Name"))
            .andExpect(jsonPath("$.bio").value("Updated bio"))
    }

    @Test
    fun `should delete user successfully`() {
        doNothing().whenever(userService).deleteUser(1L)

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should check username exists`() {
        whenever(userService.existsByUsername("testuser")).thenReturn(true)

        mockMvc.perform(get("/api/users/check/username/testuser"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exists").value(true))
    }

    @Test
    fun `should check email exists`() {
        whenever(userService.existsByEmail("test@example.com")).thenReturn(false)

        mockMvc.perform(get("/api/users/check/email/test@example.com"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exists").value(false))
    }
}