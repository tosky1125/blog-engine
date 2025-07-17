package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.UserDto
import com.example.blogenginekotlin.dto.request.UserCreateRequest
import com.example.blogenginekotlin.dto.request.UserUpdateRequest
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.exception.ForbiddenException
import com.example.blogenginekotlin.security.CurrentUser
import com.example.blogenginekotlin.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    
    @GetMapping("/me")
    fun getCurrentUser(
        @CurrentUser currentUser: User
    ): ResponseEntity<UserDto> {
        val user = userService.getUserById(currentUser.id!!)
        return ResponseEntity.ok(user)
    }
    
    @PutMapping("/me")
    fun updateCurrentUser(
        @Valid @RequestBody request: UserUpdateRequest,
        @CurrentUser currentUser: User
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUser(currentUser.id!!, request)
        return ResponseEntity.ok(updatedUser)
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(
        @Valid @RequestBody request: UserCreateRequest
    ): ResponseEntity<UserDto> {
        val user = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long
    ): ResponseEntity<UserDto> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/username/{username}")
    fun getUserByUsername(
        @PathVariable username: String
    ): ResponseEntity<UserDto> {
        val user = userService.getUserByUsername(username)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(
        @PathVariable email: String
    ): ResponseEntity<UserDto> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity.ok(user)
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<UserDto>> {
        val users = userService.getAllUsers(pageable)
        return ResponseEntity.ok(users)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest,
        @CurrentUser currentUser: User
    ): ResponseEntity<UserDto> {
        if (currentUser.id != id && currentUser.role.name != "ADMIN") {
            throw ForbiddenException("You can only update your own profile")
        }
        val updatedUser = userService.updateUser(id, request)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/check/username/{username}")
    fun checkUsernameExists(
        @PathVariable username: String
    ): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.existsByUsername(username)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @GetMapping("/check/email/{email}")
    fun checkEmailExists(
        @PathVariable email: String
    ): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.existsByEmail(email)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }
}