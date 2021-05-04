package sinhee.kang.tutorial.global.security.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtTokenFilter(
        @Autowired
        private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(request as HttpServletRequest)

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }
}
