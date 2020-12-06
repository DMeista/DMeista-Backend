package sinhee.kang.tutorial.global.config.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.domain.auth.exception.InvalidTokenException
import sinhee.kang.tutorial.global.config.security.auth.AuthDetailsService
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component(value = "jwtTokenProvider")
class JwtTokenProvider(
        private var authDetailsService: AuthDetailsService
) {
    private var secretKey: String = "@ndE^erySing1eYe@r"
    private var accessTokenExpiration: Long = 7200
    private var refreshTokenExpiration: Long = 585200
    private var header: String = "Authorization"
    private var prefix: String = "Bearer"

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