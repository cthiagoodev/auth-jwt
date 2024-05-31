package br.com.thiagoodev.authjwt.domain.services

import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.repositories.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun me(email: String): User {
         return userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
    }
}