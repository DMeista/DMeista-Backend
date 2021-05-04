package sinhee.kang.tutorial.domain.auth.service.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.repository.RefreshTokenRepository
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.global.businessException.exception.auth.ExpiredTokenException
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidTokenException
import sinhee.kang.tutorial.domain.auth.service.refreshtoken.RefreshTokenService
import sinhee.kang.tutorial.global.businessException.exception.auth.UnAuthorizedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.security.auth.AuthenticationFacade
import sinhee.kang.tutorial.global.security.jwt.JwtTokenProvider
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

@Service
class AuthServiceImpl(
        private val passwordEncoder: PasswordEncoder,
        private val authenticationFacade: AuthenticationFacade,

        private val userRepository: UserRepository,
        private val refreshTokenRepository: RefreshTokenRepository,

        private val refreshTokenService: RefreshTokenService,
        private val tokenProvider: JwtTokenProvider,

        @Value("\${auth.jwt.exp.refresh}")
        private val refreshExp: Long,

        @Value("\${auth.jwt.prefix}")
        private val tokenType: String
) : AuthService {

    override fun signIn(request: SignInRequest): TokenResponse {
        return userRepository.findByEmail(request.email)
                ?.takeIf { user -> passwordEncoder.matches(request.password, user.password) }
                ?.let { user ->
                    val accessToken: String = tokenProvider.generateAccessToken(user.nickname)
                    val refreshToken: String = tokenProvider.generateRefreshToken(user.nickname)
                    refreshTokenService.save(RefreshToken(user.nickname, refreshToken, refreshExp))
                    TokenResponse(accessToken, refreshToken, tokenType)
                }
                ?: run { throw UserNotFoundException() }
    }

    override fun refreshToken(refreshToken: String): TokenResponse {
        if (!tokenProvider.isRefreshToken(refreshToken)) {
            throw InvalidTokenException()
        }

        return refreshTokenRepository.findByRefreshToken(refreshToken)
                ?.let { receivedToken ->
                    val generatedRefreshToken = tokenProvider.generateRefreshToken(receivedToken.nickname)
                    val generatedAccessToken = tokenProvider.generateAccessToken(receivedToken.nickname)
                    receivedToken.update(generatedRefreshToken, refreshExp)
                    refreshTokenRepository.save(receivedToken)

                    TokenResponse(generatedAccessToken, generatedRefreshToken, tokenType)
                }
                ?: throw ExpiredTokenException()
    }

    override fun authValidate(): User {
        return userRepository.findByNickname(authenticationFacade.getUserName())
                ?: throw UnAuthorizedException()
    }
}
