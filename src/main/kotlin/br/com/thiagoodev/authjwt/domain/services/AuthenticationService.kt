package br.com.thiagoodev.authjwt.domain.services

import br.com.thiagoodev.authjwt.domain.dtos.LoginUserDTO
import br.com.thiagoodev.authjwt.domain.dtos.RegisterUserDTO
import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.repositories.UserRepository
import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
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
) {
    fun authentication(dto: LoginUserDTO): User {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(dto.email, dto.password))
        } catch(error: Exception) {
            throw UsernameNotFoundException("Invalid username or password")
        }

        return userRepository.findByEmail(dto.email) ?: throw UsernameNotFoundException("User not found")
    }

    fun signUp(dto: RegisterUserDTO): User {
        if(userRepository.findByEmail(dto.email) != null) {
            throw UserAlreadyExistsException()
        }

        val user = User(
            name = dto.fullName,
            email = dto.email,
            userPassword = passwordEncoder.encode(dto.password)
        )

        return userRepository.save(user)
    }
}