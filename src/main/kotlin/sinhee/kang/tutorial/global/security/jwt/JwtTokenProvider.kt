package sinhee.kang.tutorial.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.ExpiredTokenException
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.InvalidTokenException
import sinhee.kang.tutorial.global.security.authentication.AuthDetailsService
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenProvider(
    @Value("\${auth.jwt.secret}")
    private val secretKey: String,

    private val authDetailsService: AuthDetailsService,
) {

    fun getAuthentication(token: String): Authentication {
        val authDetails = authDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    fun getAccessToken(httpServletRequest: HttpServletRequest): String? =
        httpServletRequest.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer") }
            ?.substring(7)

    fun getRefreshToken(httpServletRequest: HttpServletRequest) = try {
        httpServletRequest.cookies
            .first { cookie -> cookie.name == TokenType.REFRESH.tokenName }
            .getValue()
    } catch (e: Exception) { null }

    fun getUsername(token: String): String = try {
        Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body.subject
    } catch (e: Exception) {
        throw InvalidTokenException()
    }

    fun isValidateToken(token: String): Boolean = try {
        !Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body.expiration
            .before(Date())
    } catch (e: Exception) {
        throw ExpiredTokenException()
    }

    fun isRefreshToken(token: String): Boolean = try {
        Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body["type"] == TokenType.REFRESH.tokenName
    } catch (e: Exception) {
        throw InvalidTokenException()
    }

    fun setToken(httpServletResponse: HttpServletResponse, username: String): TokenResponse {
        generateTokenFactory(username, TokenType.REFRESH)
            .let { refreshToken ->
                httpServletResponse.addCookie(generateCookieFactory(refreshToken))
            }
        val accessToken = generateTokenFactory(username, TokenType.ACCESS)

        return TokenResponse(accessToken = accessToken)
    }

    private fun generateCookieFactory(token: String): Cookie =
        Cookie(TokenType.REFRESH.tokenName, token)
            .apply {
                maxAge = TokenType.REFRESH.expiredTokenTime.toInt()
                isHttpOnly = true
                path = "/"
            }

    private fun generateTokenFactory(username: String, tokenType: TokenType): String =
        Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + tokenType.expiredTokenTime * 1000))
            .setSubject(username)
            .claim("type", tokenType.tokenName)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
}
