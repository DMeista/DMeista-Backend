package sinhee.kang.tutorial.global.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(
    private val tokenProvider: JwtTokenProvider
): OncePerRequestFilter() {

    override fun doFilterInternal(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, filterChain: FilterChain) {
        val accessToken: String? = tokenProvider.getAccessToken(httpServletRequest)
        val refreshToken: String? = tokenProvider.getRefreshToken(httpServletRequest)

        refreshToken
            ?.takeIf { tokenProvider.isValidateToken(it) && tokenProvider.isRefreshToken(it) }
            ?.apply { tokenProvider.generateAuthCookies(httpServletResponse, tokenProvider.getUsername(this)) }

        if (accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty())
            tokenProvider.generateTokenFactory(tokenProvider.getUsername(refreshToken), TokenType.ACCESS)

        accessToken
            ?.takeIf { tokenProvider.isValidateToken(it) }
            ?.apply { SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(this) }

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}
