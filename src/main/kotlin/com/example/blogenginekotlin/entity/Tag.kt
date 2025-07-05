package com.example.blogenginekotlin.entity

import jakarta.persistence.*

@Entity
@Table(name = "tags")
data class Tag(
    @Column(nullable = false, unique = true)
    var name: String,

    @Column(name = "slug", nullable = false, unique = true)
    var slug: String,

    @ManyToMany(mappedBy = "tags")
    val posts: MutableSet<Post> = mutableSetOf()
) : BaseEntity() {

    companion object {
        fun createSlug(name: String): String {
            return name.lowercase()
                .replace(Regex("[^a-z0-9가-힣\\s-]"), "")
                .replace(Regex("\\s+"), "-")
                .trim('-')
        }
    }

    @PrePersist
    @PreUpdate
    fun updateSlug() {
        if (slug.isBlank()) {
            slug = createSlug(name)
        }
    }
}