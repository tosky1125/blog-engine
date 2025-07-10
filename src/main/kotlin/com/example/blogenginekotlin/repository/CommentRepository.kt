package com.example.blogenginekotlin.repository

import com.example.blogenginekotlin.entity.Comment
import com.example.blogenginekotlin.entity.Post
import com.example.blogenginekotlin.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByPostOrderByCreatedAtDesc(post: Post, pageable: Pageable): Page<Comment>
    
    fun findAllByPostAndParentIsNullOrderByCreatedAtDesc(
        post: Post,
        pageable: Pageable
    ): Page<Comment>
    
    fun findAllByParentOrderByCreatedAtAsc(parent: Comment): List<Comment>
    
    fun findAllByAuthorOrderByCreatedAtDesc(author: User, pageable: Pageable): Page<Comment>
    
    fun countByPost(post: Post): Long
    
    fun countByAuthor(author: User): Long
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    fun countByPostId(@Param("postId") postId: Long): Long
    
    fun deleteAllByPost(post: Post)
}