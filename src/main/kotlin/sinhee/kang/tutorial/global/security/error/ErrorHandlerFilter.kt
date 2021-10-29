package sinhee.kang.tutorial.global.security.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.exception.BusinessException
import sinhee.kang.tutorial.global.exception.dto.ExceptionResponse
import sinhee.kang.tutorial.infra.api.slack.service.SlackReportService
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ErrorHandlerFilter(
    private val slackReportService: SlackReportService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BusinessException) {
            response.setBusinessException(e)
        } catch (e: Exception) {
            slackReportService.sendMessage(request, e)
            e.printStackTrace()
        }
    }

    private fun HttpServletResponse.setBusinessException(e: BusinessException) {
        contentType = MediaType.APPLICATION_JSON_VALUE
        status = e.status.value()
        writer.write(
            ObjectMapper().writer()
                .writeValueAsString(ExceptionResponse(e.status.value(), e.message))
        )
    }
}
