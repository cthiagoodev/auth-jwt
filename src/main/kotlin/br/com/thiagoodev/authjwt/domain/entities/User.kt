package br.com.thiagoodev.authjwt.domain.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    @field:UuidGenerator
    val uuid: UUID = UUID.randomUUID(),
    val name: String = "",
    @field:Column(unique = true)
    val email: String = "",
    @field:Column(name = "password")
    val userPassword: String = "",
    @field:CreatedDate
    val created: LocalDateTime = LocalDateTime.now(),
    @field:LastModifiedDate
    val updated: LocalDateTime = LocalDateTime.now()
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return userPassword
    }

    override fun getUsername(): String {
        return email
    }
}
