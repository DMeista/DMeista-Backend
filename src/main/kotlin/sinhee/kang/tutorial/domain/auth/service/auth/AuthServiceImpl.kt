package sinhee.kang.tutorial.domain.auth.service.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.global.businessException.exception.auth.UnAuthorizedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.IncorrectPasswordException
import sinhee.kang.tutorial.global.businessException.exception.common.BadRequestException
import sinhee.kang.tutorial.global.security.authentication.AuthenticationFacade
import sinhee.kang.tutorial.global.security.jwt.JwtTokenProvider
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val authenticationFacade: AuthenticationFacade,
    private val userRepository: UserRepository,
) : AuthService {

    override fun signIn(signInRequest: SignInRequest, httpServletResponse: HttpServletResponse): TokenResponse {
        val user = userRepository.findByEmail(signInRequest.email)
            ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(signInRequest.password, user.password))
            throw IncorrectPasswordException()

        return tokenProvider.setToken(httpServletResponse, user.nickname)
    }

    override fun extendAuthTokens(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): TokenResponse {
        tokenProvider.apply {
            return getRefreshToken(httpServletRequest)
                ?.takeIf { isRefreshToken(it) && isValidateToken(it) }
                ?.let { setToken(httpServletResponse, getUsername(it)) }
                ?: throw BadRequestException()
        }
    }

    override fun authValidate(): User =
        userRepository.findByNickname(authenticationFacade.getUserName())
            ?: throw UnAuthorizedException()
}
