package br.com.thiagoodev.authjwt.domain.services

import br.com.thiagoodev.authjwt.domain.dtos.LoginUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.RegisterUserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.repositories.UserRepository
import br.com.thiagoodev.authjwt.infrastructure.exceptions.InvalidFormException
import br.com.thiagoodev.authjwt.infrastructure.exceptions.RefreshTokenExpiredException
import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) {
    fun authentication(dto: LoginUserDTO): User {
        if(!dto.validate()) {
            throw InvalidFormException()
        }

        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(dto.email, dto.password))
        } catch(error: Exception) {
            throw UsernameNotFoundException("Invalid username or password")
        }

        return userRepository.findByEmail(dto.email!!) ?: throw UsernameNotFoundException("User not found")
    }

    fun signUp(dto: RegisterUserDTO): User {
        if(!dto.validate()) {
            throw InvalidFormException()
        }

        if(userRepository.findByEmail(dto.email!!) != null) {
            throw UserAlreadyExistsException()
        }

        val user = User(
            name = dto.fullName!!,
            email = dto.email,
            hashedPassword = passwordEncoder.encode(dto.password)
        )

        return userRepository.save(user)
    }

    fun refresh(refreshToken: String): String {
        val email: String = jwtService.extractUsername(refreshToken)
        val user: User = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not faund")

        val refreshExpiration: Long = jwtService.getRefreshExpiration()
        val isValid: Boolean = jwtService.isTokenValid(refreshToken, user, refreshExpiration)

        if(!isValid) throw RefreshTokenExpiredException()

        return jwtService.generateToken(user)
    }
}