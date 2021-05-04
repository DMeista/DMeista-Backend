package sinhee.kang.tutorial.global.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode
import sinhee.kang.tutorial.infra.api.slack.service.SlackExceptionService
import java.io.IOException
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionHandlerFilter(
    private val slackExceptionService: SlackExceptionService
): OncePerRequestFilter() {

    @Throws(IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BusinessException) {
            this.setErrorResponse(response, e.errorCode)
        } catch (e: Exception) {
            slackExceptionService.sendMessage(request, e)
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun setErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        val objectMapper = ObjectMapper()
        val jsonValue = objectMapper.writer()
                .writeValueAsString(errorCode)
        response.apply {
            writer.write(jsonValue)
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = errorCode.status
        }
    }
}