package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.TagDto
import com.example.blogenginekotlin.dto.request.TagCreateRequest
import com.example.blogenginekotlin.dto.request.TagUpdateRequest
import com.example.blogenginekotlin.service.TagService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun createTag(
        @Valid @RequestBody request: TagCreateRequest
    ): ResponseEntity<TagDto> {
        val tag = tagService.createTag(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(tag)
    }

    @GetMapping("/{id}")
    fun getTagById(
        @PathVariable id: Long
    ): ResponseEntity<TagDto> {
        val tag = tagService.getTagById(id)
        return ResponseEntity.ok(tag)
    }

    @GetMapping("/name/{name}")
    fun getTagByName(
        @PathVariable name: String
    ): ResponseEntity<TagDto> {
        val tag = tagService.getTagByName(name)
        return ResponseEntity.ok(tag)
    }

    @GetMapping("/slug/{slug}")
    fun getTagBySlug(
        @PathVariable slug: String
    ): ResponseEntity<TagDto> {
        val tag = tagService.getTagBySlug(slug)
        return ResponseEntity.ok(tag)
    }

    @GetMapping
    fun getAllTags(
        @PageableDefault(size = 50) pageable: Pageable
    ): ResponseEntity<Page<TagDto>> {
        val tags = tagService.getAllTags(pageable)
        return ResponseEntity.ok(tags)
    }

    @GetMapping("/popular")
    fun getPopularTags(
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<List<TagDto>> {
        val tags = tagService.getPopularTags(limit)
        return ResponseEntity.ok(tags)
    }

    @GetMapping("/search")
    fun searchTags(
        @RequestParam keyword: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<TagDto>> {
        val tags = tagService.searchTags(keyword, pageable)
        return ResponseEntity.ok(tags)
    }

    @PutMapping("/{id}")
    fun updateTag(
        @PathVariable id: Long,
        @Valid @RequestBody request: TagUpdateRequest
    ): ResponseEntity<TagDto> {
        val updatedTag = tagService.updateTag(id, request)
        return ResponseEntity.ok(updatedTag)
    }

    @DeleteMapping("/{id}")
    fun deleteTag(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        tagService.deleteTag(id)
        return ResponseEntity.noContent().build()
    }
}