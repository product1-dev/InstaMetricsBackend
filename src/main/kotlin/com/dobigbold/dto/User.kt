package com.dobigbold.dto

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class User(
    @Id
    val id: String? = null,
    val username: String,
    val password: String,
    val roles: Set<String> = setOf("USER") , // Default role is USER
    val createdAt: LocalDateTime = LocalDateTime.now(),  // Account creation time
    var isActive: Boolean = false  // Initial account status
)
