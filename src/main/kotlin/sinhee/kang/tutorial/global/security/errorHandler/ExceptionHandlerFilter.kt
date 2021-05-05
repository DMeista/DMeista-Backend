package sinhee.kang.tutorial.global.security.errorHandler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode
import sinhee.kang.tutorial.infra.api.slack.service.SlackExceptionService
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionHandlerFilter(
    private val slackExceptionService: SlackExceptionService
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BusinessException) {
            setErrorResponse(response, e.errorCode)
        } catch (e: Exception) {
            slackExceptionService.sendMessage(request, e)
            e.printStackTrace()
        }
    }

    private fun setErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        val objectMapper = ObjectMapper()
        val exception = objectMapper.writer()
                .writeValueAsString(errorCode)
        response.apply {
            writer.write(exception)
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = errorCode.status
        }
    }
}
