package br.com.thiagoodev.authjwt.infrastructure.exceptions

class RefreshTokenExpiredException(private val newMessage: String? = null) : RuntimeException() {
    override val message: String
        get() = newMessage ?: "Refresh token has expired"
}