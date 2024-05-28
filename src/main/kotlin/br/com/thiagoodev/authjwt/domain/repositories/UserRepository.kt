package br.com.thiagoodev.authjwt.domain.repositories

import br.com.thiagoodev.authjwt.domain.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun findByEmail(email: String): User?
}