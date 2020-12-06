package sinhee.kang.tutorial.global.config.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode
import java.io.IOException
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ExceptionHandlerFilter : OncePerRequestFilter() {

    @Throws(IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BusinessException) {
            this.setErrorResponse(response, e.errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun setErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        val objectMapper = ObjectMapper()
        val jsonValue = objectMapper.writer()
                .writeValueAsString(errorCode)
        response.writer.write(jsonValue)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = errorCode.status
    }
}