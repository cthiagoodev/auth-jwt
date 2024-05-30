package br.com.thiagoodev.authjwt.domain.dtos

data class RegisterUserDTO(
    val email: String,
    val password: String,
    val fullName: String,
)