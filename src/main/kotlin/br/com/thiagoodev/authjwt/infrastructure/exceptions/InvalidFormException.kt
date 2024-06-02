package br.com.thiagoodev.authjwt.infrastructure.exceptions

class InvalidFormException(private val newMessage: String? = null) : RuntimeException() {
    override val message: String
        get() = newMessage ?: "Invalid form data provided"
}