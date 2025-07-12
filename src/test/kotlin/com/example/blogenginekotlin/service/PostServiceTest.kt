package com.example.blogenginekotlin.service

import com.example.blogenginekotlin.dto.request.PostCreateRequest
import com.example.blogenginekotlin.dto.request.PostUpdateRequest
import com.example.blogenginekotlin.entity.Post
import com.example.blogenginekotlin.entity.Tag
import com.example.blogenginekotlin.entity.User
import com.example.blogenginekotlin.repository.PostRepository
import com.example.blogenginekotlin.repository.TagRepository
import com.example.blogenginekotlin.repository.UserRepository
import com.example.blogenginekotlin.service.impl.PostServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional

class PostServiceTest {

    private lateinit var postRepository: PostRepository
    private lateinit var userRepository: UserRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var postService: PostServiceImpl

    @BeforeEach
    fun setUp() {
        postRepository = mockk()
        userRepository = mockk()
        tagRepository = mockk()
        postService = PostServiceImpl(postRepository, userRepository, tagRepository)
    }

    @Test
    fun `createPost should create a new post successfully`() {
        // Given
        val userId = 1L
        val user = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { username } returns "testuser"
        }
        
        val request = PostCreateRequest(
            title = "Test Post",
            content = "Test content",
            summary = "Test summary",
            coverImageUrl = "http://example.com/image.jpg",
            tagNames = listOf("tag1", "tag2"),
            isPublished = true
        )

        val tag1 = mockk<Tag>(relaxed = true) {
            every { id } returns 1L
            every { name } returns "tag1"
        }
        val tag2 = mockk<Tag>(relaxed = true) {
            every { id } returns 2L
            every { name } returns "tag2"
        }

        val savedPost = mockk<Post>(relaxed = true) {
            every { id } returns 1L
            every { title } returns request.title
            every { published } returns true
            every { publishedAt } returns LocalDateTime.now()
            every { tags } returns mutableSetOf(tag1, tag2)
            every { author } returns user
            every { likeCount } returns 0
            every { commentCount } returns 0
        }

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { tagRepository.findByName("tag1") } returns tag1
        every { tagRepository.findByName("tag2") } returns tag2
        every { postRepository.save(any()) } returns savedPost

        // When
        val result = postService.createPost(userId, request)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals(savedPost.title, result.title)
        assertEquals(2, result.tags.size)
        assertTrue(result.isPublished)
        verify { postRepository.save(any()) }
    }

    @Test
    fun `getPostById should return post when found`() {
        // Given
        val postId = 1L
        val user = mockk<User>(relaxed = true) {
            every { id } returns 1L
            every { username } returns "testuser"
        }
        val post = mockk<Post>(relaxed = true) {
            every { id } returns postId
            every { title } returns "Test"
            every { published } returns true
            every { author } returns user
            every { tags } returns mutableSetOf()
            every { comments } returns mutableListOf()
        }

        every { postRepository.findByIdWithDetails(postId) } returns post

        // When
        val result = postService.getPostById(postId)

        // Then
        assertNotNull(result)
        assertEquals(postId, result.id)
        assertEquals("Test", result.title)
    }

    @Test
    fun `publishPost should publish unpublished post`() {
        // Given
        val postId = 1L
        val userId = 1L
        val user = mockk<User>(relaxed = true) {
            every { id } returns userId
        }
        val post = mockk<Post>(relaxed = true) {
            every { id } returns postId
            every { published } returns false andThen true
            every { author } returns user
            every { likeCount } returns 0
            every { commentCount } returns 0
            every { tags } returns mutableSetOf()
        }

        every { postRepository.findById(postId) } returns Optional.of(post)
        every { postRepository.save(any()) } returns post

        // When
        val result = postService.publishPost(postId, userId)

        // Then
        assertNotNull(result)
        verify { postRepository.save(post) }
    }

    @Test
    fun `publishPost should throw exception when user not authorized`() {
        // Given
        val postId = 1L
        val userId = 2L // Different user
        val author = mockk<User>(relaxed = true) {
            every { id } returns 1L
        }
        val post = mockk<Post>(relaxed = true)
        every { post.id } returns postId
        every { post.published } returns false
        every { post.author } returns author

        every { postRepository.findById(postId) } returns Optional.of(post)

        // When & Then
        val exception = assertThrows<IllegalStateException> {
            postService.publishPost(postId, userId)
        }
        assertEquals("User $userId is not authorized to publish this post", exception.message)
    }

    @Test
    fun `deletePost should delete post when user is author`() {
        // Given
        val postId = 1L
        val userId = 1L
        val user = mockk<User>(relaxed = true) {
            every { id } returns userId
        }
        val post = mockk<Post>(relaxed = true) {
            every { id } returns postId
            every { author } returns user
        }

        every { postRepository.findById(postId) } returns Optional.of(post)
        every { postRepository.deleteById(postId) } returns Unit

        // When
        postService.deletePost(postId, userId)

        // Then
        verify { postRepository.deleteById(postId) }
    }

    @Test
    fun `getPublishedPosts should return only published posts`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val user = mockk<User>(relaxed = true)
        val post1 = mockk<Post>(relaxed = true) {
            every { id } returns 1L
            every { title } returns "Post1"
            every { published } returns true
            every { author } returns user
            every { tags } returns mutableSetOf()
            every { likeCount } returns 0
            every { commentCount } returns 0
        }
        val post2 = mockk<Post>(relaxed = true) {
            every { id } returns 2L
            every { title } returns "Post2"
            every { published } returns true
            every { author } returns user
            every { tags } returns mutableSetOf()
            every { likeCount } returns 0
            every { commentCount } returns 0
        }
        val posts = listOf(post1, post2)
        val page = PageImpl(posts, pageable, posts.size.toLong())

        every { postRepository.findByPublishedTrue(pageable) } returns page

        // When
        val result = postService.getPublishedPosts(pageable)

        // Then
        assertEquals(2, result.content.size)
        assertTrue(result.content.all { it.isPublished })
    }

    @Test
    fun `searchPosts should return matching posts`() {
        // Given
        val keyword = "test"
        val pageable = PageRequest.of(0, 10)
        val user = mockk<User>(relaxed = true)
        val post = mockk<Post>(relaxed = true) {
            every { id } returns 1L
            every { title } returns "Test Post"
            every { published } returns true
            every { author } returns user
            every { tags } returns mutableSetOf()
            every { likeCount } returns 0
            every { commentCount } returns 0
        }
        val posts = listOf(post)
        val page = PageImpl(posts, pageable, posts.size.toLong())

        every { postRepository.searchByKeyword(keyword, pageable) } returns page

        // When
        val result = postService.searchPosts(keyword, pageable)

        // Then
        assertEquals(1, result.content.size)
        assertTrue(result.content[0].title.contains("Test"))
    }
}