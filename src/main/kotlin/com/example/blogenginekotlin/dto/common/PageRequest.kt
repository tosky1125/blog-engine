package com.example.blogenginekotlin.dto.common

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PageRequest(
    @field:Min(value = 0, message = "Page number must be non-negative")
    val page: Int = 0,

    @field:Min(value = 1, message = "Page size must be at least 1")
    @field:Max(value = 100, message = "Page size must not exceed 100")
    val size: Int = 20,

    val sort: String? = null,
    val direction: SortDirection = SortDirection.DESC
) {
    enum class SortDirection {
        ASC, DESC
    }
}