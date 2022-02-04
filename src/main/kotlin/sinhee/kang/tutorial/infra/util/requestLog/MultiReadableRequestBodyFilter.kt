package sinhee.kang.tutorial.infra.util.requestLog

import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class MultiReadableRequestBodyFilter : Filter {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        val wrappedRequest = MultiReadableHttpServletRequest(servletRequest as HttpServletRequest)

        filterChain.doFilter(wrappedRequest, servletResponse)
    }
}
