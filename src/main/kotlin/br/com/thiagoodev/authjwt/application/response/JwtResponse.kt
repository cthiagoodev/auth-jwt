package br.com.thiagoodev.authjwt.application.response

data class JwtResponse(
    val access: String,
    val expiration: Long,
)
