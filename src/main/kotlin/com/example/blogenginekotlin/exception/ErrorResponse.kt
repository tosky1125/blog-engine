package com.example.blogenginekotlin.exception

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val errorCode: String,
    val message: String,
    val path: String,
    val fieldErrors: List<FieldError>? = null
) {
    data class FieldError(
        val field: String,
        val value: Any?,
        val message: String
    )
}