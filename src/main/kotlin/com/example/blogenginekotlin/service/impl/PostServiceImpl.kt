package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.PostDto
import com.example.blogenginekotlin.dto.PostDetailDto
import com.example.blogenginekotlin.dto.request.PostCreateRequest
import com.example.blogenginekotlin.dto.request.PostUpdateRequest
import com.example.blogenginekotlin.entity.Post
import com.example.blogenginekotlin.entity.Tag
import com.example.blogenginekotlin.repository.PostRepository
import com.example.blogenginekotlin.repository.TagRepository
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) : PostService {

    @Transactional
    override fun createPost(userId: Long, request: PostCreateRequest): PostDto {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }

        val tags = request.tagNames?.map { tagName ->
            tagRepository.findByName(tagName) ?: tagRepository.save(Tag(
                name = tagName,
                slug = Tag.createSlug(tagName)
            ))
        }?.toMutableSet() ?: mutableSetOf()

        val post = Post(
            title = request.title,
            content = request.content,
            summary = request.summary ?: "",
            coverImageUrl = request.coverImageUrl,
            author = user,
            tags = tags,
            published = request.isPublished ?: false,
            publishedAt = if (request.isPublished == true) LocalDateTime.now() else null
        )

        val savedPost = postRepository.save(post)
        return PostDto.from(savedPost)
    }

    override fun getPostById(id: Long): PostDetailDto {
        val post = postRepository.findByIdWithDetails(id)
            ?: throw NoSuchElementException("Post not found with id: $id")
        return PostDetailDto.from(post)
    }

    override fun getPublishedPostById(id: Long): PostDetailDto {
        val post = postRepository.findByIdAndPublishedTrue(id)
            ?: throw NoSuchElementException("Published post not found with id: $id")
        return PostDetailDto.from(post)
    }

    @Transactional
    override fun updatePost(id: Long, userId: Long, request: PostUpdateRequest): PostDto {
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }

        if (post.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to update this post")
        }

        request.title?.let { post.title = it }
        request.content?.let { post.content = it }
        request.summary?.let { post.summary = it }
        request.coverImageUrl?.let { post.coverImageUrl = it }

        request.tagNames?.let { tagNames ->
            val newTags = tagNames.map { tagName ->
                tagRepository.findByName(tagName) ?: tagRepository.save(Tag(
                    name = tagName,
                    slug = Tag.createSlug(tagName)
                ))
            }.toMutableSet()
            post.tags.clear()
            post.tags.addAll(newTags)
        }

        val updatedPost = postRepository.save(post)
        return PostDto.from(updatedPost)
    }

    @Transactional
    override fun deletePost(id: Long, userId: Long) {
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }

        if (post.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to delete this post")
        }

        postRepository.deleteById(id)
    }

    @Transactional
    override fun publishPost(id: Long, userId: Long): PostDto {
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }

        if (post.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to publish this post")
        }

        if (post.published) {
            throw IllegalStateException("Post is already published")
        }

        post.published = true
        post.publishedAt = LocalDateTime.now()

        val publishedPost = postRepository.save(post)
        return PostDto.from(publishedPost)
    }

    @Transactional
    override fun unpublishPost(id: Long, userId: Long): PostDto {
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }

        if (post.author.id != userId) {
            throw IllegalStateException("User $userId is not authorized to unpublish this post")
        }

        if (!post.published) {
            throw IllegalStateException("Post is already unpublished")
        }

        post.published = false
        post.publishedAt = null

        val unpublishedPost = postRepository.save(post)
        return PostDto.from(unpublishedPost)
    }

    override fun getAllPosts(pageable: Pageable): Page<PostDto> {
        return postRepository.findAll(pageable).map { PostDto.from(it) }
    }

    override fun getPublishedPosts(pageable: Pageable): Page<PostDto> {
        return postRepository.findAllByPublishedTrueOrderByPublishedAtDesc(pageable).map { PostDto.from(it) }
    }

    override fun getPostsByUser(userId: Long, pageable: Pageable): Page<PostDto> {
        val author = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }
        return postRepository.findAllByAuthorOrderByCreatedAtDesc(author, pageable).map { PostDto.from(it) }
    }

    override fun getPublishedPostsByUser(userId: Long, pageable: Pageable): Page<PostDto> {
        val author = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with id: $userId") }
        return postRepository.findAllByAuthorAndPublishedTrueOrderByPublishedAtDesc(author, pageable).map { PostDto.from(it) }
    }

    override fun getPostsByTag(tagName: String, pageable: Pageable): Page<PostDto> {
        val tagSlug = Tag.createSlug(tagName)
        return postRepository.findAllByTagSlugAndPublishedTrue(tagSlug, pageable).map { PostDto.from(it) }
    }

    override fun searchPosts(keyword: String, pageable: Pageable): Page<PostDto> {
        return postRepository.searchByKeyword(keyword, pageable).map { PostDto.from(it) }
    }

    @Transactional
    override fun incrementViewCount(id: Long) {
        postRepository.incrementViewCount(id)
    }
}