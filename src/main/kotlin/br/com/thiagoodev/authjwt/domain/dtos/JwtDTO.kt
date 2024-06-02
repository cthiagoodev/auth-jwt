package br.com.thiagoodev.authjwt.domain.dtos

data class JwtDTO(
    val access: String,
    val refresh: String,
    val expiration: Long,
)
