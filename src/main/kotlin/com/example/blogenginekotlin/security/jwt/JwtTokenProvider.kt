package com.example.blogenginekotlin.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
    
    @Value("\${jwt.expiration}")
    private val jwtExpirationInMs: Long,
    
    @Value("\${jwt.refresh-expiration}")
    private val refreshExpirationInMs: Long
) {
    
    private val key: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        Keys.hmacShaKeyFor(keyBytes)
    }
    
    fun generateAccessToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetails
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)
        
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun generateAccessToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)
        
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun generateRefreshToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshExpirationInMs)
        
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .claim("type", "refresh")
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        
        return claims.subject
    }
    
    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
            return true
        } catch (ex: SecurityException) {
            return false
        } catch (ex: MalformedJwtException) {
            return false
        } catch (ex: ExpiredJwtException) {
            return false
        } catch (ex: UnsupportedJwtException) {
            return false
        } catch (ex: IllegalArgumentException) {
            return false
        }
    }
    
    fun isRefreshToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
            
            return claims["type"] == "refresh"
        } catch (ex: Exception) {
            return false
        }
    }
}