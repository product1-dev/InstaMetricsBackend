package com.dobigbold.repositories

import com.dobigbold.dto.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
    fun findByUserId(username: String): Mono<User>
}