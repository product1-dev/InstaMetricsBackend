package com.dobigbold.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import io.jsonwebtoken.Claims
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

object JwtUtil {

    private val secretKey = "your-secret-key"
    private val expirationTime = 86400000  // 1 day in milliseconds

    private val keyPair = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }.genKeyPair()

    private val privateKey: PrivateKey = keyPair.private
    private val publicKey: PublicKey = keyPair.public

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val claims = getClaimsFromToken(token)
        val tokenUsername = claims.subject
        val expirationDate = claims.expiration
        return (tokenUsername == username && !expirationDate.before(Date()))
    }
    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body  // This is where 'body' might cause an issue if not handled correctly
    }
    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
