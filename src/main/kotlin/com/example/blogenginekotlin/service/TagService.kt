package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.TagDto
import com.example.blogenginekotlin.dto.request.TagCreateRequest
import com.example.blogenginekotlin.dto.request.TagUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TagService {
    fun createTag(request: TagCreateRequest): TagDto
    fun getTagById(id: Long): TagDto
    fun getTagByName(name: String): TagDto
    fun getTagBySlug(slug: String): TagDto
    fun updateTag(id: Long, request: TagUpdateRequest): TagDto
    fun deleteTag(id: Long)
    fun getAllTags(pageable: Pageable): Page<TagDto>
    fun getPopularTags(limit: Int): List<TagDto>
    fun searchTags(keyword: String, pageable: Pageable): Page<TagDto>
}