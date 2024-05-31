package br.com.thiagoodev.authjwt.application.controllers

import br.com.thiagoodev.authjwt.application.errors.ResponseErrorMessage
import br.com.thiagoodev.authjwt.domain.dtos.RegisterUserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.services.AuthenticationService
import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val jwtService: JwtService,
    private val authenticationService: AuthenticationService,
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody registerDTO: RegisterUserDTO): ResponseEntity<User> {
        try {
            val registeredUser: User = authenticationService.signUp(registerDTO)
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
        } catch(error: UserAlreadyExistsException) {
            return ResponseErrorMessage(HttpStatus.CONFLICT, error.message).build()
        } catch(error: Exception) {
            val message: String = error.message ?: "Error creating user"
            return ResponseErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, message).build()
        }
    }
}