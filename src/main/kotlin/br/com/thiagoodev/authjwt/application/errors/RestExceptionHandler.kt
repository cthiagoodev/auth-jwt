package br.com.thiagoodev.authjwt.application.errors

import br.com.thiagoodev.authjwt.infrastructure.exceptions.InvalidFormException
import br.com.thiagoodev.authjwt.infrastructure.exceptions.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun genericException(exception: Exception): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "Bad Request"
        return ResponseErrorMessage(HttpStatus.BAD_REQUEST, message).build()
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun userNotFoundException(exception: RuntimeException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "User not found"
        return ResponseErrorMessage(HttpStatus.NOT_FOUND, message).build()
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun userAlreadyExistsException(exception: RuntimeException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "User Already Exists"
        return ResponseErrorMessage(HttpStatus.CONFLICT, message).build()
    }

    @ExceptionHandler(InvalidFormException::class)
    fun invalidDataException(exception: RuntimeException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.message ?: "Invalid data"
        return ResponseErrorMessage(HttpStatus.BAD_REQUEST, message).build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(exception: BindException): ResponseEntity<ResponseErrorMessage> {
        val message: String = exception.fieldError?.defaultMessage ?: exception.message
        return ResponseErrorMessage(HttpStatus.CONFLICT, message).build()
    }
}