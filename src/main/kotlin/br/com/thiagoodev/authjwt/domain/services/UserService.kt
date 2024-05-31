package br.com.thiagoodev.authjwt.domain.services

import br.com.thiagoodev.authjwt.domain.entities.User
import br.com.thiagoodev.authjwt.domain.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {
    fun find(email: String): User {
         return userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
    }

    fun find(uuid: UUID): User {
        return userRepository.findByIdOrNull(uuid.toString()) ?: throw UsernameNotFoundException("User not found")
    }
}