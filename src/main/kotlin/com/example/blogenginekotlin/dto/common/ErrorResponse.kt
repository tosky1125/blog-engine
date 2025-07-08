package com.example.blogenginekotlin.dto.common

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val details: Map<String, Any>? = null
)