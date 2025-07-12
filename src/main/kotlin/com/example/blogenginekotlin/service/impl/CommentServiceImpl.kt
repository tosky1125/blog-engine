package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.CommentDto
import com.example.blogenginekotlin.dto.request.CommentCreateRequest
import com.example.blogenginekotlin.dto.request.CommentUpdateRequest
import com.example.blogenginekotlin.entity.Comment
import com.example.blogenginekotlin.repository.CommentRepository
import com.example.blogenginekotlin.repository.PostRepository
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.CommentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : CommentService {

    @Transactional
    override fun createComment(userId: Long, postId: Long, request: CommentCreateRequest): CommentDto {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }
        
        val post = postRepository.findById(postId)
            .orElseThrow { NoSuchElementException("Post not found with id: $postId") }

        if (!post.isPublished) {
            throw IllegalStateException("Cannot comment on unpublished posts")
        }

        val comment = Comment(
            content = request.content,
            author = user,
            post = post
        )

        val savedComment = commentRepository.save(comment)
        return CommentDto.from(savedComment)
    }

    @Transactional
    override fun createReply(userId: Long, parentCommentId: Long, request: CommentCreateRequest): CommentDto {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }
        
        val parentComment = commentRepository.findById(parentCommentId)
            .orElseThrow { NoSuchElementException("Parent comment not found with id: $parentCommentId") }

        if (!parentComment.post.isPublished) {
            throw IllegalStateException("Cannot reply to comments on unpublished posts")
        }

        val reply = Comment(
            content = request.content,
            author = user,
            post = parentComment.post,
            parentComment = parentComment
        )

        val savedReply = commentRepository.save(reply)
        return CommentDto.from(savedReply)
    }

    override fun getCommentById(id: Long): CommentDto {
        val comment = commentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Comment not found with id: $id") }
        return CommentDto.from(comment)
    }

    @Transactional
    override fun updateComment(id: Long, userId: Long, request: CommentUpdateRequest): CommentDto {
        val comment = commentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Comment not found with id: $id") }

        if (comment.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to update this comment")
        }

        comment.content = request.content
        comment.isEdited = true

        val updatedComment = commentRepository.save(comment)
        return CommentDto.from(updatedComment)
    }

    @Transactional
    override fun deleteComment(id: Long, userId: Long) {
        val comment = commentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Comment not found with id: $id") }

        if (comment.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to delete this comment")
        }

        commentRepository.deleteById(id)
    }

    override fun getCommentsByPost(postId: Long, pageable: Pageable): Page<CommentDto> {
        return commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable)
            .map { CommentDto.from(it) }
    }

    override fun getRepliesByComment(commentId: Long, pageable: Pageable): Page<CommentDto> {
        return commentRepository.findByParentCommentId(commentId, pageable)
            .map { CommentDto.from(it) }
    }

    override fun getCommentsByUser(userId: Long, pageable: Pageable): Page<CommentDto> {
        return commentRepository.findByAuthorId(userId, pageable)
            .map { CommentDto.from(it) }
    }
}