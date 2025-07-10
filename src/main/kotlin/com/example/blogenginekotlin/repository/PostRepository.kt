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

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByPublishedTrueOrderByPublishedAtDesc(pageable: Pageable): Page<Post>
    
    fun findAllByAuthorAndPublishedTrueOrderByPublishedAtDesc(
        author: User,
        pageable: Pageable
    ): Page<Post>
    
    fun findAllByAuthorOrderByCreatedAtDesc(
        author: User,
        pageable: Pageable
    ): Page<Post>
    
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.slug = :tagSlug AND p.published = true ORDER BY p.publishedAt DESC")
    fun findAllByTagSlugAndIsPublishedTrue(
        @Param("tagSlug") tagSlug: String,
        pageable: Pageable
    ): Page<Post>
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.published = true ORDER BY p.publishedAt DESC")
    fun searchByKeyword(
        @Param("keyword") keyword: String,
        pageable: Pageable
    ): Page<Post>
    
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    fun incrementViewCount(@Param("postId") postId: Long)
    
    fun countByAuthor(author: User): Long
    
    fun countByAuthorAndPublishedTrue(author: User): Long
}