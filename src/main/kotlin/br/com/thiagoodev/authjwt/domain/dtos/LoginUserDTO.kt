package br.com.thiagoodev.authjwt.domain.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginUserDTO(
    @field:NotNull
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotNull
    @field:NotBlank
    val password: String,
)
