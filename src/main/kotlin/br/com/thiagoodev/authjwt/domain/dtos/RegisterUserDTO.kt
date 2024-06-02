package br.com.thiagoodev.authjwt.domain.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RegisterUserDTO(
    @field:NotBlank(message = "Email is mandatory")
    @field:NotNull(message = "Email is mandatory")
    @field:Email(message = "Email should be valid")
    val email: String?,
    @field:NotBlank(message = "Password is mandatory")
    @field:NotNull(message = "Password is mandatory")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password: String?,
    @field:NotBlank(message = "Username is mandatory")
    @field:NotNull(message = "Username is mandatory")
    val fullName: String?,
) {
    fun validate(): Boolean {
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                !fullName.isNullOrEmpty()
    }
}
