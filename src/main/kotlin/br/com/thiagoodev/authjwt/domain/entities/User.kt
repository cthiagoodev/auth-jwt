package br.com.thiagoodev.authjwt.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    val uuid: String = "",
    val name: String = "",
    @field:Column(unique = true)
    val email: String = "",
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
