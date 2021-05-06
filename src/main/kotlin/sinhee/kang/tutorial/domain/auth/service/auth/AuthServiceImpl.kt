package sinhee.kang.tutorial.domain.auth.service.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken

import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.global.businessException.exception.auth.UnAuthorizedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.IncorrectPasswordException
import sinhee.kang.tutorial.global.security.authentication.AuthenticationFacade
import sinhee.kang.tutorial.global.security.jwt.JwtTokenProvider
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException
import sinhee.kang.tutorial.global.security.jwt.enums.TokenType
import javax.servlet.http.HttpServletResponse

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val authenticationFacade: AuthenticationFacade,
) : AuthService {

    override fun signIn(request: SignInRequest, response: HttpServletResponse): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: run { throw UserNotFoundException() }
        if (!passwordEncoder.matches(request.password, user.password))
            throw IncorrectPasswordException()
        return tokenProvider.generateTokenAndCookie(response, user.nickname)
    }

    override fun authValidate(): User {
        return userRepository.findByNickname(authenticationFacade.getUserName())
                ?: throw UnAuthorizedException()
    }
}
