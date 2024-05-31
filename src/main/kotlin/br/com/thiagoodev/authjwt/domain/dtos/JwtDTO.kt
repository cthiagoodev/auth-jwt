package br.com.thiagoodev.authjwt.domain.dtos

data class JwtDTO(
    val access: String,
    val expiration: Long,
)
