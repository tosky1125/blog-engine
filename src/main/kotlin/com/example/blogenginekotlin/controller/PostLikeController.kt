package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.PostLikeDto
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.security.CurrentUser
import com.example.blogenginekotlin.service.PostLikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts/{postId}/likes")
class PostLikeController(
    private val postLikeService: PostLikeService
) {

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun likePost(
        @PathVariable postId: Long,
        @CurrentUser currentUser: User
    ): ResponseEntity<PostLikeDto> {
        val postLike = postLikeService.likePost(currentUser.id!!, postId)
        return ResponseEntity.status(HttpStatus.CREATED).body(postLike)
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    fun unlikePost(
        @PathVariable postId: Long,
        @CurrentUser currentUser: User
    ): ResponseEntity<Void> {
        postLikeService.unlikePost(currentUser.id!!, postId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/check")
    @PreAuthorize("isAuthenticated()")
    fun checkIfLiked(
        @PathVariable postId: Long,
        @CurrentUser currentUser: User
    ): ResponseEntity<Map<String, Boolean>> {
        val isLiked = postLikeService.isPostLikedByUser(currentUser.id!!, postId)
        return ResponseEntity.ok(mapOf("liked" to isLiked))
    }

    @GetMapping("/count")
    fun getLikeCount(
        @PathVariable postId: Long
    ): ResponseEntity<Map<String, Long>> {
        val count = postLikeService.getLikeCountByPost(postId)
        return ResponseEntity.ok(mapOf("count" to count))
    }

    @GetMapping("/users/{userId}")
    fun getLikedPostsByUser(
        @PathVariable userId: Long
    ): ResponseEntity<Map<String, List<Long>>> {
        val postIds = postLikeService.getLikedPostIdsByUser(userId)
        return ResponseEntity.ok(mapOf("postIds" to postIds))
    }
}