package com.example.blogenginekotlin.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class TagCreateRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(max = 50, message = "Tag name must not exceed 50 characters")
    val name: String,

    @field:Size(max = 200, message = "Description must not exceed 200 characters")
    val description: String?
)