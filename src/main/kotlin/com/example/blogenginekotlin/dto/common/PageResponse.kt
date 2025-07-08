package com.example.blogenginekotlin.dto.common

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val numberOfElements: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
) {
    companion object {
        fun <T> of(page: Page<T>): PageResponse<T> {
            return PageResponse(
                content = page.content,
                currentPage = page.number,
                totalPages = page.totalPages,
                totalElements = page.totalElements,
                size = page.size,
                numberOfElements = page.numberOfElements,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty
            )
        }

        fun <T, R> of(page: Page<T>, mapper: (T) -> R): PageResponse<R> {
            return PageResponse(
                content = page.content.map(mapper),
                currentPage = page.number,
                totalPages = page.totalPages,
                totalElements = page.totalElements,
                size = page.size,
                numberOfElements = page.numberOfElements,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty
            )
        }
    }
}