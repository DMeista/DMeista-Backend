package sinhee.kang.tutorial.infra.util.requestLog

import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest


@Component
class MultiReadableRequestBodyFilter : Filter {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        if (servletRequest.contentType == null) {
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }
        if (servletRequest.contentType.startsWith("multipart/form-data")) {
            filterChain.doFilter(servletRequest, servletResponse)
        } else {
            val wrappedRequest = WrappedRequest(servletRequest as HttpServletRequest)
            filterChain.doFilter(wrappedRequest, servletResponse)
        }
    }
}
