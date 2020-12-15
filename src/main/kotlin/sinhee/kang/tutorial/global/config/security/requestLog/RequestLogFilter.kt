package sinhee.kang.tutorial.global.config.security.requestLog

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestLogFilter: OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val wrappedRequest = WrappedRequest(request)
        this.doFilter(wrappedRequest, response, filterChain)
    }

}