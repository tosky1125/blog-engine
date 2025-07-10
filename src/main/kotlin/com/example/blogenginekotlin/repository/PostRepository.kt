package com.example.blogenginekotlin.repository

import com.example.blogenginekotlin.entity.Post
import com.example.blogenginekotlin.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface PostRepository : JpaRepository<Post, UUID> {
    fun findBySlug(slug: String): Optional<Post>
    
    fun findAllByIsPublishedTrueOrderByPublishedAtDesc(pageable: Pageable): Page<Post>
    
    fun findAllByAuthorAndIsPublishedTrueOrderByPublishedAtDesc(
        author: User,
        pageable: Pageable
    ): Page<Post>
    
    fun findAllByAuthorOrderByCreatedAtDesc(
        author: User,
        pageable: Pageable
    ): Page<Post>
    
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.slug = :tagSlug AND p.isPublished = true ORDER BY p.publishedAt DESC")
    fun findAllByTagSlugAndIsPublishedTrue(
        @Param("tagSlug") tagSlug: String,
        pageable: Pageable
    ): Page<Post>
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isPublished = true ORDER BY p.publishedAt DESC")
    fun searchByKeyword(
        @Param("keyword") keyword: String,
        pageable: Pageable
    ): Page<Post>
    
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    fun incrementViewCount(@Param("postId") postId: UUID)
    
    fun countByAuthor(author: User): Long
    
    fun countByAuthorAndIsPublishedTrue(author: User): Long
}