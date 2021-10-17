package sinhee.kang.tutorial.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.MethodNotAllowedException
import sinhee.kang.tutorial.global.exception.BusinessException
import sinhee.kang.tutorial.global.exception.dto.ExceptionResponse
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidArgumentException

@ControllerAdvice
class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handledBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            status = e.status.value(),
            message = e.message,
            e.status
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handledInvalidArgumentException(e: HttpMessageNotReadableException): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            status = InvalidArgumentException().status.value(),
            message = InvalidArgumentException().message,
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MethodArgumentNotValidException::class, MethodNotAllowedException::class)
    fun handledInvalidMethodException(): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),
            message = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase,
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED
        )

    fun generateResponseEntity(status: Int, message: String, httpStatus: HttpStatus) =
        ResponseEntity(
            ExceptionResponse(status, message),
            httpStatus
        )
}

