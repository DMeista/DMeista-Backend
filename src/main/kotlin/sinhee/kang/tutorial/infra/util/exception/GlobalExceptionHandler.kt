package sinhee.kang.tutorial.infra.util.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.server.ServerErrorException
import sinhee.kang.tutorial.infra.api.slack.service.SlackReportService
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException
import sinhee.kang.tutorial.infra.util.exception.exceptions.dto.ExceptionResponse
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class GlobalExceptionHandler(
    private val slackReportService: SlackReportService
) {

    @ExceptionHandler(BusinessException::class)
    private fun handledBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            httpStatus = e.status,
            message = e.message
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handledInvalidArgumentException(e: HttpMessageNotReadableException): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            httpStatus = HttpStatus.BAD_REQUEST,
            message = InvalidArgumentException().message
        )

    @ExceptionHandler(MethodArgumentNotValidException::class, MethodNotAllowedException::class)
    fun handledInvalidMethodException(): ResponseEntity<ExceptionResponse> =
        generateResponseEntity(
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED,
            message = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase
        )

    @ExceptionHandler(Exception::class)
    fun handledExistException(e: Exception): ResponseEntity<ExceptionResponse> {
        slackReportService.sendMessage(e)
        return generateResponseEntity(
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
        )
    }

    private fun generateResponseEntity(httpStatus: HttpStatus, message: String) =
        ResponseEntity(
            ExceptionResponse(message),
            httpStatus
        )
}
