package br.com.thiagoodev.authjwt.application.controllers

import br.com.thiagoodev.authjwt.domain.dtos.JwtDTO
import br.com.thiagoodev.authjwt.domain.dtos.LoginUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.RegisterUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.UserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.services.AuthenticationService
import br.com.thiagoodev.authjwt.infrastructure.extensions.fromUser
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
        val registeredUser: User = authenticationService.signUp(form)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromUser(registeredUser))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody form: LoginUserDTO): ResponseEntity<JwtDTO> {
        val user: User = authenticationService.authentication(form)

        val expiration: Long = jwtService.getExpiration()
        val refreshExpiration: Long = jwtService.getRefreshExpiration()

        val token: String = jwtService.generateToken(user)
        val refreshToken: String = jwtService.generateToken(user, refreshExpiration)

        val response = JwtDTO(token, refreshToken, expiration)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") authorization: String): ResponseEntity<JwtDTO> {
        val refreshedToken: String = authenticationService.refresh(authorization)
        val expiration: Long = jwtService.getExpiration()
        val response = JwtDTO(refreshedToken, authorization.substring(7), expiration)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}