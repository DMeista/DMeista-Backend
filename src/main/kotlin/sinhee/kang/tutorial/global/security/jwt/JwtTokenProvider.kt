package sinhee.kang.tutorial.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.domain.auth.exception.InvalidTokenException
import sinhee.kang.tutorial.global.security.auth.AuthDetailsService
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

    fun generateAccessToken(username: String): String {
        return Jwts.builder()
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .setSubject(username)
                .claim("type", "access_token")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .setSubject(username)
                .claim("type", "refresh_token")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(header)
        if (bearerToken != null && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(7)
        }
        return null
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).body.subject
            return true
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun getAuthentication(token: String): Authentication {
        val authDetails = authDetailsService.loadUserByUsername(getUserNickname(token))
        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    fun getUserNickname(token: String): String {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun isRefreshToken(token: String): Boolean {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).body["type"] == "refresh_token"
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }
}