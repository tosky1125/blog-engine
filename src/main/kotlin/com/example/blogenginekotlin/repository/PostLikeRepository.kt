package com.example.blogenginekotlin.repository

import com.example.blogenginekotlin.entity.Post
import com.example.blogenginekotlin.entity.PostLike
import com.example.blogenginekotlin.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun findByUserAndPost(user: User, post: Post): Optional<PostLike>
    
    fun existsByUserAndPost(user: User, post: Post): Boolean
    
    fun countByPost(post: Post): Long
    
    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    fun countByPostId(@Param("postId") postId: Long): Long
    
    fun findAllByUser(user: User): List<PostLike>
    
    fun deleteByUserAndPost(user: User, post: Post)
    
    fun deleteAllByPost(post: Post)
}