package com.example.blogenginekotlin.security.service

import com.example.blogenginekotlin.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
    }
    
    @Transactional(readOnly = true)
    fun loadUserById(id: Long): UserDetails {
        return userRepository.findById(id).orElseThrow {
            UsernameNotFoundException("User not found with id: $id")
        }
    }
}