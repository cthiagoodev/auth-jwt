package br.com.thiagoodev.authjwt.domain.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginUserDTO(
    @field:NotNull(message = "Email is required")
    @field:NotBlank(message = "Email field cannot be empty")
    @field:Email(message = "Please provide a valid email address")
    val email: String?,
    @field:NotNull(message = "Password is required")
    @field:NotBlank(message = "Password field cannot be empty")
    val password: String?,
) {
    fun validate(): Boolean {
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }
}
