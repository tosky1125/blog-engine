package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.PostLikeDto
import com.example.blogenginekotlin.entity.PostLike
import com.example.blogenginekotlin.repository.PostLikeRepository
import com.example.blogenginekotlin.repository.PostRepository
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.PostLikeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostLikeServiceImpl(
    private val postLikeRepository: PostLikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : PostLikeService {

    @Transactional
    override fun likePost(userId: Long, postId: Long): PostLikeDto {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }
        
        val post = postRepository.findById(postId)
            .orElseThrow { NoSuchElementException("Post not found with id: $postId") }

        if (!post.published) {
            throw IllegalStateException("Cannot like unpublished posts")
        }

        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw IllegalStateException("User has already liked this post")
        }

        val postLike = PostLike(
            user = user,
            post = post
        )

        val savedPostLike = postLikeRepository.save(postLike)
        
        post.likeCount = postLikeRepository.countByPostId(postId).toInt()
        postRepository.save(post)

        return PostLikeDto.from(savedPostLike)
    }

    @Transactional
    override fun unlikePost(userId: Long, postId: Long) {
        val postLike = postLikeRepository.findByUserIdAndPostId(userId, postId)
            ?: throw NoSuchElementException("Like not found for user $userId and post $postId")

        postLikeRepository.delete(postLike)
        
        val post = postRepository.findById(postId)
            .orElseThrow { NoSuchElementException("Post not found with id: $postId") }
        
        post.likeCount = postLikeRepository.countByPostId(postId).toInt()
        postRepository.save(post)
    }

    override fun isPostLikedByUser(userId: Long, postId: Long): Boolean {
        return postLikeRepository.existsByUserIdAndPostId(userId, postId)
    }

    override fun getLikeCountByPost(postId: Long): Long {
        return postLikeRepository.countByPostId(postId)
    }

    override fun getLikedPostIdsByUser(userId: Long): List<Long> {
        return postLikeRepository.findPostIdsByUserId(userId)
    }
}