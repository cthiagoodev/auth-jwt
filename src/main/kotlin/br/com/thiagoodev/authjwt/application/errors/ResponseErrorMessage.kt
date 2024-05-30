package br.com.thiagoodev.authjwt.application.errors

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity

class ResponseErrorMessage(
    private val statusCode: HttpStatus,
    private val message: String,
) {
    fun <T> build(): ResponseEntity<T> {
        val status = HttpStatusCode.valueOf(statusCode.value())
        val detail = ProblemDetail.forStatusAndDetail(status, message)
        return ResponseEntity.of(detail).build()
    }
}

