package br.com.thiagoodev.authjwt.application.controllers

import br.com.thiagoodev.authjwt.application.errors.ResponseErrorMessage
import br.com.thiagoodev.authjwt.domain.dtos.UserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.services.UserService
import br.com.thiagoodev.authjwt.infrastructure.extensions.fromUser
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService,
) {
    @GetMapping("/me")
    fun me(@RequestHeader("Authorization") authorization: String): ResponseEntity<UserDTO> {
        try {
            val email: String = jwtService.extractUsername(authorization)
            val user: User = userService.me(email)
            return ResponseEntity.ok(UserDTO.fromUser(user))
        } catch(error: UsernameNotFoundException) {
            val message: String = error.message ?: "User not exists"
            return ResponseErrorMessage(HttpStatus.NOT_FOUND, message).build()
        } catch(error: Exception) {
            val message: String = error.message ?: "Error on get user"
            return ResponseErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, message).build()
        }
    }
}