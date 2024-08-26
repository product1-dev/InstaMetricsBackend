package com.dobigbold.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRegistrationRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password should be at least 6 characters")
    val password: String
)
