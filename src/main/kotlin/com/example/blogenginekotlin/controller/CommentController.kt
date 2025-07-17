package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.CommentDto
import com.example.blogenginekotlin.dto.request.CommentCreateRequest
import com.example.blogenginekotlin.dto.request.CommentUpdateRequest
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.security.CurrentUser
import com.example.blogenginekotlin.service.CommentService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    fun createComment(
        @PathVariable postId: Long,
        @CurrentUser currentUser: User,
        @Valid @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentDto> {
        val comment = commentService.createComment(currentUser.id!!, postId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(comment)
    }

    @PostMapping("/{parentCommentId}/replies")
    @PreAuthorize("isAuthenticated()")
    fun createReply(
        @PathVariable parentCommentId: Long,
        @CurrentUser currentUser: User,
        @Valid @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentDto> {
        val reply = commentService.createReply(currentUser.id!!, parentCommentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(reply)
    }

    @GetMapping("/{id}")
    fun getCommentById(
        @PathVariable id: Long
    ): ResponseEntity<CommentDto> {
        val comment = commentService.getCommentById(id)
        return ResponseEntity.ok(comment)
    }

    @GetMapping("/posts/{postId}")
    fun getCommentsByPost(
        @PathVariable postId: Long,
        @PageableDefault(size = 50) pageable: Pageable
    ): ResponseEntity<Page<CommentDto>> {
        val comments = commentService.getCommentsByPost(postId, pageable)
        return ResponseEntity.ok(comments)
    }

    @GetMapping("/{commentId}/replies")
    fun getRepliesByComment(
        @PathVariable commentId: Long,
        @PageableDefault(size = 50) pageable: Pageable
    ): ResponseEntity<Page<CommentDto>> {
        val replies = commentService.getRepliesByComment(commentId, pageable)
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/users/{userId}")
    fun getCommentsByUser(
        @PathVariable userId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<CommentDto>> {
        val comments = commentService.getCommentsByUser(userId, pageable)
        return ResponseEntity.ok(comments)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun updateComment(
        @PathVariable id: Long,
        @CurrentUser currentUser: User,
        @Valid @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentDto> {
        val updatedComment = commentService.updateComment(id, currentUser.id!!, request)
        return ResponseEntity.ok(updatedComment)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteComment(
        @PathVariable id: Long,
        @CurrentUser currentUser: User
    ): ResponseEntity<Void> {
        commentService.deleteComment(id, currentUser.id!!)
        return ResponseEntity.noContent().build()
    }
}