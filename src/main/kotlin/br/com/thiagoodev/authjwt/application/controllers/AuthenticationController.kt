package br.com.thiagoodev.authjwt.application.controllers

import br.com.thiagoodev.authjwt.application.errors.ResponseErrorMessage
import br.com.thiagoodev.authjwt.domain.dtos.JwtDTO
import br.com.thiagoodev.authjwt.domain.dtos.LoginUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.RegisterUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.UserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.services.AuthenticationService
import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.authjwt.infrastructure.extensions.fromUser
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
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
    fun register(@Valid @RequestBody form: RegisterUserDTO): ResponseEntity<UserDTO> {
        try {
            val registeredUser: User = authenticationService.signUp(form)
            return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromUser(registeredUser))
        } catch(error: UserAlreadyExistsException) {
            return ResponseErrorMessage(HttpStatus.CONFLICT, error.message).build()
        } catch(error: Exception) {
            val message: String = error.message ?: "Error creating user"
            return ResponseErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, message).build()
        }
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody form: LoginUserDTO): ResponseEntity<JwtDTO> {
        try {
            val user: User = authenticationService.authentication(form)
            val token: String = jwtService.generateToken(user)
            val expiration: Long = jwtService.getExpiration()
            val response = JwtDTO(token, expiration)

            return ResponseEntity.ok(response)
        } catch(error: UsernameNotFoundException) {
            val message: String = error.message ?: "User not exists"
            return ResponseErrorMessage(HttpStatus.NOT_FOUND, message).build()
        } catch(error: Exception) {
            val message: String = error.message ?: "Error on generate token"
            return ResponseErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, message).build()
        }
    }
}