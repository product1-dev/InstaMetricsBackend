package com.dobigbold.service

import com.dobigbold.dto.User
import com.dobigbold.repositories.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun saveUser(user: User) {
        userRepository.save(user)
    }

    fun activateUser(userId: String): Mono<String> {
        return userRepository.findByUserId(userId)
            .flatMap { user ->
                user.isActive = true
                userRepository.save(user)
                    .thenReturn("User activated successfully")
            }
            .switchIfEmpty(Mono.error(Exception("User not found")))
    }
}

