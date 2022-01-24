package sinhee.kang.tutorial.application.service.token

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.application.dto.response.auth.TokenResponse
import sinhee.kang.tutorial.application.service.token.enums.TokenType
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.ExpiredTokenException
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenProvider(
    @Value("\${auth.jwt.secret}")
    private val secretKey: String,

    private val httpServletRequest: HttpServletRequest
) {

    fun getAccessToken(): String? =
        httpServletRequest.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer") }
            ?.substring(7)

    fun getRefreshToken(): String? = try {
        httpServletRequest.cookies
            .first { it.name == TokenType.REFRESH.tokenName }
            .value
    } catch (e: Exception) { null }

    fun verifyToken(token: String): String = try {
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body["userName"].toString()
    } catch (e: ExpiredJwtException) {
        throw ExpiredTokenException()
    } catch (e: Exception) {
        throw BadRequestException()
    }

    fun setToken(httpServletResponse: HttpServletResponse, username: String): TokenResponse {
        generateToken(username, TokenType.REFRESH)
            .let { refreshToken ->
                httpServletResponse.addCookie(generateCookie(refreshToken))
            }
        val accessToken = generateToken(username, TokenType.ACCESS)

        return TokenResponse(accessToken = accessToken)
    }

    private fun generateToken(username: String, tokenType: TokenType): String {
        val currentTime = Date()

        return Jwts.builder()
            .setSubject("auth")
            .claim("userName", username)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .setIssuedAt(currentTime)
            .setExpiration(currentTime.apply {
                time += tokenType.expiredTokenTime }
            )
            .compact()
    }

    private fun generateCookie(token: String): Cookie =
        Cookie(TokenType.REFRESH.tokenName, token)
            .apply {
                maxAge = TokenType.REFRESH.expiredTokenTime.toInt()
                isHttpOnly = true
                path = "/"
            }
}
