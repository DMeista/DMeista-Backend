package sinhee.kang.tutorial.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken
import sinhee.kang.tutorial.domain.auth.service.refreshtoken.RefreshTokenService
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidTokenException
import sinhee.kang.tutorial.global.security.authentication.AuthDetailsService
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Component(value = "jwtTokenProvider")
class JwtTokenProvider(
    private val authDetailsService: AuthDetailsService,

    private val refreshTokenService: RefreshTokenService,

    @Value("\${auth.jwt.secret}")
    private val secretKey: String,
) {

    fun getAuthentication(token: String): Authentication {
        val authDetails = authDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    fun getUsername(token: String): String = try {
        Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            throw InvalidTokenException()
        }

    fun isValidateToken(token: String): Boolean {
        return try {
            !Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).body.expiration
                .before(Date())
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun generateAuthCookies(httpServletResponse: HttpServletResponse, username: String) {
        val refreshToken = generateTokenFactory(username, TokenType.REFRESH)
            .apply { refreshTokenService.save(RefreshToken(username, this, TokenType.REFRESH.expiredTokenTime)) }
        val accessToken = generateTokenFactory(username, TokenType.ACCESS)

        httpServletResponse.apply {
            addCookie(generateCookieFactory(TokenType.REFRESH, refreshToken))
            addCookie(generateCookieFactory(TokenType.ACCESS, accessToken))
        }
    }

    private fun generateCookieFactory(tokenType: TokenType, token: String): Cookie =
        Cookie(tokenType.cookieName, token).apply {
            maxAge = tokenType.expiredTokenTime.toInt()
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
