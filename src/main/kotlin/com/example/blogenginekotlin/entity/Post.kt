package com.example.blogenginekotlin.entity

import jakarta.persistence.*
import org.hibernate.annotations.Formula
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
data class Post(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, length = 1000)
    var summary: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Column(name = "cover_image_url")
    var coverImageUrl: String? = null,

    @Column(nullable = false)
    var published: Boolean = false,

    @Column(name = "published_at")
    var publishedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "post_tags",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: MutableSet<Tag> = mutableSetOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<PostLike> = mutableListOf(),

    @Column(name = "view_count", nullable = false)
    var viewCount: Long = 0,

    @Column(name = "like_count", nullable = false)
    var likeCount: Int = 0,

    @Formula("(SELECT COUNT(c.id) FROM comments c WHERE c.post_id = id)")
    val commentCount: Int = 0
) : BaseEntity() {

    fun publish() {
        this.published = true
        this.publishedAt = LocalDateTime.now()
    }

    fun unpublish() {
        this.published = false
        this.publishedAt = null
    }

    fun addTag(tag: Tag) {
        tags.add(tag)
        tag.posts.add(this)
    }

    fun removeTag(tag: Tag) {
        tags.remove(tag)
        tag.posts.remove(this)
    }

    fun incrementViewCount() {
        this.viewCount++
    }
}