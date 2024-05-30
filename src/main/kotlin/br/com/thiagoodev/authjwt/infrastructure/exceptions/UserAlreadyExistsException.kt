package br.com.thiagoodev.authjwt.infrastructure.exceptions

class UserAlreadyExistsException(private val newMessage: String? = null) : Exception() {
    override val message: String
        get() = newMessage ?: "User already exists"
}