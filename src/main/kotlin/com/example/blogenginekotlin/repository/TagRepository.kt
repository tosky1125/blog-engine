package com.example.blogenginekotlin.repository

import com.example.blogenginekotlin.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?
    
    fun findBySlug(slug: String): Tag?
    
    fun existsByName(name: String): Boolean
    
    fun existsBySlug(slug: String): Boolean
    
    @Query("SELECT t FROM Tag t JOIN t.posts p WHERE p.published = true GROUP BY t ORDER BY COUNT(p) DESC")
    fun findMostUsedTags(): List<Tag>
    
    @Query("SELECT t, COUNT(p) as postCount FROM Tag t JOIN t.posts p WHERE p.published = true GROUP BY t ORDER BY postCount DESC")
    fun findTagsWithPostCount(): List<Array<Any>>
    
    fun findAllByOrderByNameAsc(): List<Tag>
    
    @Query("SELECT t FROM Tag t JOIN t.posts p WHERE p.published = true GROUP BY t ORDER BY COUNT(p) DESC")
    fun findPopularTags(pageable: org.springframework.data.domain.Pageable): List<Tag>
    
    @Query("SELECT t FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    fun searchByKeyword(keyword: String, pageable: org.springframework.data.domain.Pageable): org.springframework.data.domain.Page<Tag>
}