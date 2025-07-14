package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.PostLikeDto
import com.example.blogenginekotlin.service.PostLikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts/{postId}/likes")
class PostLikeController(
    private val postLikeService: PostLikeService
) {

    @PostMapping
    fun likePost(
        @PathVariable postId: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<PostLikeDto> {
        val postLike = postLikeService.likePost(userId, postId)
        return ResponseEntity.status(HttpStatus.CREATED).body(postLike)
    }

    @DeleteMapping
    fun unlikePost(
        @PathVariable postId: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<Void> {
        postLikeService.unlikePost(userId, postId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/check")
    fun checkIfLiked(
        @PathVariable postId: Long,
        @RequestHeader("X-User-Id") userId: Long // Temporary - will be replaced with auth
    ): ResponseEntity<Map<String, Boolean>> {
        val isLiked = postLikeService.isPostLikedByUser(userId, postId)
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