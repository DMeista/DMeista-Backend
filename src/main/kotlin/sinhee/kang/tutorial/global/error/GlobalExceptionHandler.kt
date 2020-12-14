package sinhee.kang.tutorial.global.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode
import sinhee.kang.tutorial.infra.api.slack.SlackSenderManager

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
                ErrorResponse(ErrorCode.BAD_REQUEST.status, "Invalid Parameter"),
                HttpStatus.valueOf(ErrorCode.BAD_REQUEST.status))
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorCode: ErrorCode = e.errorCode
        return ResponseEntity(
                ErrorResponse(errorCode.status, e.message),
                HttpStatus.valueOf(errorCode.status))
    }
}