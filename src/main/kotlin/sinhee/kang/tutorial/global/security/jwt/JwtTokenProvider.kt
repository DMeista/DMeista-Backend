package sinhee.kang.tutorial.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.repository.RefreshTokenRepository
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.auth.service.refreshtoken.RefreshTokenService
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidTokenException
import sinhee.kang.tutorial.global.security.authentication.AuthDetailsService
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenProvider(
    private val authDetailsService: AuthDetailsService,

    private val refreshTokenService: RefreshTokenService,

    private val refreshTokenRepository: RefreshTokenRepository,

    @Value("\${auth.jwt.secret}")
    private val secretKey: String
) {

    fun getAccessToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else null
    }

    fun getRefreshToken(httpServletRequest: HttpServletRequest) = try {
        httpServletRequest.cookies
            .first { cookie -> cookie.name == TokenType.REFRESH.cookieName }.value
        } catch (e: Exception) { null }

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

    fun isValidateToken(token: String): Boolean = try {
        !Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body.expiration
            .before(Date())
        } catch (e: Exception) {
            throw InvalidTokenException()
        }

    fun isRefreshToken(token: String): Boolean = try {
        Jwts.parser().setSigningKey(secretKey)
            .parseClaimsJws(token).body["type"] == TokenType.REFRESH.tokenName
        } catch (e: Exception) {
            throw InvalidTokenException()
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

    fun generateTokenAndCookie(httpServletResponse: HttpServletResponse, username: String): TokenResponse {
        val refreshToken = generateTokenFactory(username, TokenType.REFRESH)
            .apply { refreshTokenService.save(RefreshToken(username, this, TokenType.REFRESH.expiredTokenTime)) }
        val accessToken = generateTokenFactory(username, TokenType.ACCESS)

        httpServletResponse.addCookie(generateCookieFactory(TokenType.REFRESH, refreshToken))

        return TokenResponse(accessToken)
    }

    fun generateCookieFactory(tokenType: TokenType, token: String): Cookie =
        Cookie(tokenType.cookieName, token).apply {
            maxAge = tokenType.expiredTokenTime.toInt()
            isHttpOnly = true
            secure = true
            path = "/"
        }

    fun generateTokenFactory(username: String, tokenType: TokenType): String =
        Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + tokenType.expiredTokenTime * 1000))
            .setSubject(username)
            .claim("type", tokenType.tokenName)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
}
