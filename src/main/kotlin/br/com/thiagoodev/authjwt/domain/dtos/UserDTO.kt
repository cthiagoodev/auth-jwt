package br.com.thiagoodev.authjwt.domain.dtos

import java.time.LocalDateTime
import java.util.UUID

data class UserDTO(
    val uuid: UUID,
    val name: String,
    val email: String,
    val enabled: Boolean,
    val created: LocalDateTime,
    val updated: LocalDateTime,
) {
    companion object
}
