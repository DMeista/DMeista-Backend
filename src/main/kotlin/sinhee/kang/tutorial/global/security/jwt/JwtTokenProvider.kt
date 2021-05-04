package sinhee.kang.tutorial.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidTokenException
import sinhee.kang.tutorial.global.security.auth.AuthDetailsService
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component(value = "jwtTokenProvider")
class JwtTokenProvider(
        private val authDetailsService: AuthDetailsService,

        @Value("\${auth.jwt.secret}")
        private val secretKey: String,

        @Value("\${auth.jwt.exp.access}")
        private val accessTokenExpiration: Long,

        @Value("\${auth.jwt.exp.refresh}")
        private val refreshTokenExpiration: Long,

        @Value("\${auth.jwt.header}")
        private val header: String,

        @Value("\${auth.jwt.prefix}")
        private val prefix: String
) {

    fun generateAccessToken(username: String): String =
        generateTokenFactory(username, accessTokenExpiration, TokenType.ACCESS)

    fun generateRefreshToken(username: String): String =
        generateTokenFactory(username, refreshTokenExpiration, TokenType.REFRESH)

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(header)
        return if (bearerToken != null && bearerToken.startsWith(prefix)) {
            bearerToken.substring(7)
        } else null
    }

    fun isValidateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).body.subject
            return true
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun isRefreshToken(token: String): Boolean =
        try {
            Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).body["type"] == TokenType.REFRESH
        } catch (e: Exception) {
            throw InvalidTokenException()
        }

    fun getAuthentication(token: String): Authentication {
        val authDetails = authDetailsService.loadUserByUsername(getUserNickname(token))
        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    private fun generateTokenFactory(username: String, tokenExpiration: Long, tokenType: TokenType) =
        Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + tokenExpiration * 1000))
            .setSubject(username)
            .claim("type", tokenType)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    private fun getUserNickname(token: String): String =
        try {
            Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
}
