package com.example.blogenginekotlin.dto.user

import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:Size(max = 100, message = "Name must not exceed 100 characters")
    val name: String? = null,

    @field:Size(max = 500, message = "Bio must not exceed 500 characters")
    val bio: String? = null,

    val profileImageUrl: String? = null
)