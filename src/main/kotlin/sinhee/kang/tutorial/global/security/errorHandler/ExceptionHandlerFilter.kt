package sinhee.kang.tutorial.global.security.errorHandler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.exception.BusinessException
import sinhee.kang.tutorial.infra.api.slack.service.SlackReportService
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionHandlerFilter(
    private val slackReportService: SlackReportService
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BusinessException) {
            setErrorResponse(response, e.status)
        } catch (e: Exception) {
            slackReportService.sendMessage(request, e)
            e.printStackTrace()
        }
    }

    private fun setErrorResponse(response: HttpServletResponse, httpStatus: HttpStatus) {
        val objectMapper = ObjectMapper()
        val exception = objectMapper.writer()
                .writeValueAsString(httpStatus)
        response.apply {
            writer.write(exception)
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = httpStatus.value()
        }
    }
}
