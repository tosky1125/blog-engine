package com.example.blogenginekotlin.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    private val password: String,

    @Column(nullable = false)
    val name: String,

    @Column(length = 500)
    val bio: String? = null,

    @Column(name = "profile_image_url")
    val profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.USER,

    @Column(nullable = false)
    val enabled: Boolean = true,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf()
) : BaseEntity(), UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}

enum class UserRole {
    USER, ADMIN, MODERATOR
}