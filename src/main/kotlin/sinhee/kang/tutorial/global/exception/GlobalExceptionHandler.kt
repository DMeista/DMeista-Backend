package sinhee.kang.tutorial.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.MethodNotAllowedException

import sinhee.kang.tutorial.global.exception.dto.response.ExceptionResponse
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(
                status = e.status.value(),
                message = e.message),
            HttpStatus.valueOf(e.status.value())
        )

    @ExceptionHandler(MethodArgumentNotValidException::class, MethodNotAllowedException::class)
    protected fun invalidMethodException(): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(
                status = HttpStatus.METHOD_NOT_ALLOWED.value(),
                message = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase),
            HttpStatus.METHOD_NOT_ALLOWED
        )

    @ExceptionHandler(MissingServletRequestParameterException::class, HttpMessageNotReadableException::class)
    protected fun servletRequestParameterException(e: BadRequestException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(
                status = e.status.value(),
                message = e.message),
            e.status
        )
}
