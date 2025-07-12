package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.CommentDto
import com.example.blogenginekotlin.dto.request.CommentCreateRequest
import com.example.blogenginekotlin.dto.request.CommentUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentService {
    fun createComment(userId: Long, postId: Long, request: CommentCreateRequest): CommentDto
    fun createReply(userId: Long, parentCommentId: Long, request: CommentCreateRequest): CommentDto
    fun getCommentById(id: Long): CommentDto
    fun updateComment(id: Long, userId: Long, request: CommentUpdateRequest): CommentDto
    fun deleteComment(id: Long, userId: Long)
    fun getCommentsByPost(postId: Long, pageable: Pageable): Page<CommentDto>
    fun getRepliesByComment(commentId: Long, pageable: Pageable): Page<CommentDto>
    fun getCommentsByUser(userId: Long, pageable: Pageable): Page<CommentDto>
}