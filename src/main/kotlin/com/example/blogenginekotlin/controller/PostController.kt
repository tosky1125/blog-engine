package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.PostDto
import com.example.blogenginekotlin.dto.PostDetailDto
import com.example.blogenginekotlin.dto.request.PostCreateRequest
import com.example.blogenginekotlin.dto.request.PostUpdateRequest
import com.example.blogenginekotlin.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(
        @RequestHeader("X-User-Id") userId: Long, // Temporary - will be replaced with auth
        @Valid @RequestBody request: PostCreateRequest
    ): ResponseEntity<PostDto> {
        val post = postService.createPost(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(post)
    }

    @GetMapping("/{id}")
    fun getPostById(
        @PathVariable id: Long
    ): ResponseEntity<PostDetailDto> {
        val post = postService.getPostById(id)
        postService.incrementViewCount(id)
        return ResponseEntity.ok(post)
    }

    @GetMapping("/published/{id}")
    fun getPublishedPostById(
        @PathVariable id: Long
    ): ResponseEntity<PostDetailDto> {
        val post = postService.getPublishedPostById(id)
        postService.incrementViewCount(id)
        return ResponseEntity.ok(post)
    }

    @GetMapping
    fun getAllPosts(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.getAllPosts(pageable)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/published")
    fun getPublishedPosts(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.getPublishedPosts(pageable)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/user/{userId}")
    fun getPostsByUser(
        @PathVariable userId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.getPostsByUser(userId, pageable)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/user/{userId}/published")
    fun getPublishedPostsByUser(
        @PathVariable userId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.getPublishedPostsByUser(userId, pageable)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/tag/{tagName}")
    fun getPostsByTag(
        @PathVariable tagName: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.getPostsByTag(tagName, pageable)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/search")
    fun searchPosts(
        @RequestParam keyword: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<PostDto>> {
        val posts = postService.searchPosts(keyword, pageable)
        return ResponseEntity.ok(posts)
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestHeader("X-User-Id") userId: Long, // Temporary - will be replaced with auth
        @Valid @RequestBody request: PostUpdateRequest
    ): ResponseEntity<PostDto> {
        val updatedPost = postService.updatePost(id, userId, request)
        return ResponseEntity.ok(updatedPost)
    }

    @PutMapping("/{id}/publish")
    fun publishPost(
        @PathVariable id: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<PostDto> {
        val publishedPost = postService.publishPost(id, userId)
        return ResponseEntity.ok(publishedPost)
    }

    @PutMapping("/{id}/unpublish")
    fun unpublishPost(
        @PathVariable id: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<PostDto> {
        val unpublishedPost = postService.unpublishPost(id, userId)
        return ResponseEntity.ok(unpublishedPost)
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<Void> {
        postService.deletePost(id, userId)
        return ResponseEntity.noContent().build()
    }
}