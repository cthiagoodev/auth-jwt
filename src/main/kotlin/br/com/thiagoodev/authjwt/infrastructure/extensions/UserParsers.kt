package br.com.thiagoodev.authjwt.infrastructure.extensions

import br.com.thiagoodev.authjwt.domain.dtos.UserDTO
import br.com.thiagoodev.authjwt.domain.entities.User

fun UserDTO.Companion.fromUser(user: User): UserDTO {
    return UserDTO(
        uuid = user.uuid,
        name = user.name,
        email = user.email,
        enabled = user.isEnabled,
        created = user.created,
        updated = user.updated,
    )
}