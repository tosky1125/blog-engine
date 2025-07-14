package com.example.blogenginekotlin.controller

import com.example.blogenginekotlin.dto.*
import com.example.blogenginekotlin.dto.request.PostCreateRequest
import com.example.blogenginekotlin.dto.request.PostUpdateRequest
import com.example.blogenginekotlin.exception.ForbiddenException
import com.example.blogenginekotlin.exception.ResourceNotFoundException
import com.example.blogenginekotlin.service.PostService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(PostController::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var postService: PostService

    @Test
    fun `should create post successfully`() {
        val request = PostCreateRequest(
            title = "Test Post",
            content = "Test content",
            summary = "Test summary",
            coverImageUrl = null,
            isPublished = false,
            tagNames = listOf("tag1", "tag2")
        )

        val tags = listOf(
            TagDto(
                id = 1L,
                name = "tag1",
                slug = "tag1",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            ),
            TagDto(
                id = 2L,
                name = "tag2", 
                slug = "tag2",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            )
        )

        val postDto = PostDto(
            id = 1L,
            title = "Test Post",
            content = "Test content",
            summary = "Test summary",
            coverImageUrl = null,
            authorId = 1L,
            authorUsername = "testuser",
            isPublished = false,
            publishedAt = null,
            viewCount = 0,
            likeCount = 0,
            commentCount = 0,
            tags = tags,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(postService.createPost(any(), any())).thenReturn(postDto)

        mockMvc.perform(
            post("/api/posts")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test Post"))
    }

    @Test
    fun `should get post by id and increment view count`() {
        val author = UserDto(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            name = "Test User",
            bio = null,
            avatarUrl = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )

        val tags = listOf(
            TagDto(
                id = 1L,
                name = "tag1",
                slug = "tag1",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            )
        )

        val postDetail = PostDetailDto(
            id = 1L,
            title = "Test Post",
            content = "Test content",
            summary = "Test summary",
            coverImageUrl = null,
            author = author,
            isPublished = true,
            publishedAt = LocalDateTime.now(),
            viewCount = 10,
            likeCount = 5,
            tags = tags,
            comments = emptyList(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(postService.getPostById(1L)).thenReturn(postDetail)
        doNothing().whenever(postService).incrementViewCount(1L)

        mockMvc.perform(get("/api/posts/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test Post"))
            .andExpect(jsonPath("$.content").value("Test content"))

        verify(postService).incrementViewCount(1L)
    }

    @Test
    fun `should get published posts with pagination`() {
        val tags = listOf(
            TagDto(
                id = 1L,
                name = "tag1",
                slug = "tag1",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            )
        )

        val posts = listOf(
            PostDto(
                id = 1L,
                title = "Post 1",
                content = "Content 1",
                summary = "Summary 1",
                coverImageUrl = null,
                authorId = 1L,
                authorUsername = "user1",
                isPublished = true,
                publishedAt = LocalDateTime.now(),
                viewCount = 100,
                likeCount = 10,
                commentCount = 5,
                tags = tags,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val pageable = PageRequest.of(0, 20)
        val page = PageImpl(posts, pageable, 1)

        whenever(postService.getPublishedPosts(any())).thenReturn(page)

        mockMvc.perform(get("/api/posts/published"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].isPublished").value(true))
    }

    @Test
    fun `should search posts by keyword`() {
        val tags = listOf(
            TagDto(
                id = 1L,
                name = "spring",
                slug = "spring",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            ),
            TagDto(
                id = 2L,
                name = "tutorial",
                slug = "tutorial",
                postCount = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = null
            )
        )

        val posts = listOf(
            PostDto(
                id = 1L,
                title = "Spring Boot Tutorial",
                content = "Learn Spring Boot",
                summary = "Learn Spring Boot",
                coverImageUrl = null,
                authorId = 1L,
                authorUsername = "author1",
                isPublished = true,
                publishedAt = LocalDateTime.now(),
                viewCount = 50,
                likeCount = 5,
                commentCount = 2,
                tags = tags,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val pageable = PageRequest.of(0, 20)
        val page = PageImpl(posts, pageable, 1)

        whenever(postService.searchPosts(any(), any())).thenReturn(page)

        mockMvc.perform(get("/api/posts/search").param("keyword", "Spring"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].title").value("Spring Boot Tutorial"))
    }

    @Test
    fun `should publish post successfully`() {
        val publishedPost = PostDto(
            id = 1L,
            title = "Test Post",
            content = "Test content",
            summary = "Test summary",
            coverImageUrl = null,
            authorId = 1L,
            authorUsername = "testuser",
            isPublished = true,
            publishedAt = LocalDateTime.now(),
            viewCount = 0,
            likeCount = 0,
            commentCount = 0,
            tags = emptyList(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(postService.publishPost(1L, 1L)).thenReturn(publishedPost)

        mockMvc.perform(
            put("/api/posts/1/publish")
                .header("X-User-Id", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isPublished").value(true))
    }

    @Test
    fun `should return 403 when trying to publish others post`() {
        whenever(postService.publishPost(1L, 2L))
            .thenThrow(ForbiddenException("You don't have permission to publish this post"))

        mockMvc.perform(
            put("/api/posts/1/publish")
                .header("X-User-Id", "2")
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should delete post successfully`() {
        doNothing().whenever(postService).deletePost(1L, 1L)

        mockMvc.perform(
            delete("/api/posts/1")
                .header("X-User-Id", "1")
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when post not found`() {
        whenever(postService.getPostById(999L))
            .thenThrow(ResourceNotFoundException("Post", "id", 999L))

        mockMvc.perform(get("/api/posts/999"))
            .andExpect(status().isNotFound)
    }
}