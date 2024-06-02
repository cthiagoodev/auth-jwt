package br.com.thiagoodev.authjwt.application.controllers

import br.com.thiagoodev.authjwt.domain.dtos.UserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.services.UserService
import br.com.thiagoodev.authjwt.infrastructure.extensions.fromUser
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import org.springframework.http.ResponseEntity
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
    @GetMapping("/details")
    fun details(@RequestHeader("Authorization") authorization: String): ResponseEntity<UserDTO> {
        val email: String = jwtService.extractUsername(authorization)
        val user: User = userService.find(email)
        return ResponseEntity.ok(UserDTO.fromUser(user))
    }
}