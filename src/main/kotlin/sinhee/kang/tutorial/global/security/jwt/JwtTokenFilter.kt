package sinhee.kang.tutorial.global.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(
    private val tokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(servletRequest: HttpServletRequest, servletResponse: HttpServletResponse, chain: FilterChain) {
        tokenProvider.getAccessToken(servletRequest)
            ?.takeIf { tokenProvider.isValidateToken(it) }
            ?.also { SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(it) }

        chain.doFilter(servletRequest, servletResponse)
    }
}
