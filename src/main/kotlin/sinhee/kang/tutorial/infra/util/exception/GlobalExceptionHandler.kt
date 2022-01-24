package sinhee.kang.tutorial.infra.util.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.MethodNotAllowedException
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException
import sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException::class)
    private fun handledBusinessException(e: sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException): ResponseEntity<sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse> =
        generateResponseEntity(
            httpStatus = e.status,
            message = e.message
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handledInvalidArgumentException(e: HttpMessageNotReadableException): ResponseEntity<sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse> =
        generateResponseEntity(
            httpStatus = HttpStatus.BAD_REQUEST,
            message = sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException().message
        )

    @ExceptionHandler(MethodArgumentNotValidException::class, MethodNotAllowedException::class)
    fun handledInvalidMethodException(): ResponseEntity<sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse> =
        generateResponseEntity(
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED,
            message = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase
        )

    private fun generateResponseEntity(httpStatus: HttpStatus, message: String) =
        ResponseEntity(
            sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse(message),
            httpStatus
        )
}
