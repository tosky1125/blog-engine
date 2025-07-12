package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.PostLikeDto

interface PostLikeService {
    fun likePost(userId: Long, postId: Long): PostLikeDto
    fun unlikePost(userId: Long, postId: Long)
    fun isPostLikedByUser(userId: Long, postId: Long): Boolean
    fun getLikeCountByPost(postId: Long): Long
    fun getLikedPostIdsByUser(userId: Long): List<Long>
}