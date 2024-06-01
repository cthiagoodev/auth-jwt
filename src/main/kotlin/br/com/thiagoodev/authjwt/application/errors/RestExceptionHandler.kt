package br.com.thiagoodev.authjwt.application.errors

import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun genericException(exception: Exception): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "Bad Request"
        return ResponseErrorMessage(HttpStatus.BAD_REQUEST, message).build()
    }

    @ExceptionHandler
    fun userNotFoundException(exception: UsernameNotFoundException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "User not found"
        return ResponseErrorMessage(HttpStatus.BAD_REQUEST, message).build()
    }

    @ExceptionHandler
    fun userAlreadyExistsException(exception: UserAlreadyExistsException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message
        return ResponseErrorMessage(HttpStatus.BAD_REQUEST, message).build()
    }
}