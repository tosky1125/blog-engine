package com.example.blogenginekotlin.entity

import jakarta.persistence.*

@Entity
@Table(name = "comments")
data class Comment(
    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent: Comment? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val replies: MutableList<Comment> = mutableListOf(),

    @Column(nullable = false)
    var isEdited: Boolean = false
) : BaseEntity()