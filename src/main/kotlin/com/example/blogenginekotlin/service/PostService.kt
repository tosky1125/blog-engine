package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.PostDto
import com.example.blogenginekotlin.dto.PostDetailDto
import com.example.blogenginekotlin.dto.request.PostCreateRequest
import com.example.blogenginekotlin.dto.request.PostUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService {
    fun createPost(userId: Long, request: PostCreateRequest): PostDto
    fun getPostById(id: Long): PostDetailDto
    fun getPublishedPostById(id: Long): PostDetailDto
    fun updatePost(id: Long, userId: Long, request: PostUpdateRequest): PostDto
    fun deletePost(id: Long, userId: Long)
    fun publishPost(id: Long, userId: Long): PostDto
    fun unpublishPost(id: Long, userId: Long): PostDto
    fun getAllPosts(pageable: Pageable): Page<PostDto>
    fun getPublishedPosts(pageable: Pageable): Page<PostDto>
    fun getPostsByUser(userId: Long, pageable: Pageable): Page<PostDto>
    fun getPublishedPostsByUser(userId: Long, pageable: Pageable): Page<PostDto>
    fun getPostsByTag(tagName: String, pageable: Pageable): Page<PostDto>
    fun searchPosts(keyword: String, pageable: Pageable): Page<PostDto>
    fun incrementViewCount(id: Long)
}