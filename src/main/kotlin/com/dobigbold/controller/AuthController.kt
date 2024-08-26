package com.dobigbold.controller

import com.dobigbold.dto.LoginRequest
import com.dobigbold.dto.User
import com.dobigbold.dto.UserRegistrationRequest
import com.dobigbold.repositories.UserRepository
import com.dobigbold.service.UserService
import com.dobigbold.utils.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody registrationRequest: UserRegistrationRequest): ResponseEntity<String> {
        val userId = UUID.randomUUID().toString()
        val newUser = User(
            id = userId,
            username = registrationRequest.username,
            password = registrationRequest.password,
            createdAt = LocalDateTime.now(),
            isActive = false  // Initially inactive
        )
        userService.saveUser(newUser)
        return ResponseEntity.ok("User registered successfully. Await admin activation.")
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<String> {
        return userRepository.findByUsername(loginRequest.username)
            .filter { passwordEncoder.matches(loginRequest.password, it.password) }
            .map { JwtUtil.generateToken(it.username) }
            .switchIfEmpty(Mono.error(Exception("Invalid credentials")))
    }
}