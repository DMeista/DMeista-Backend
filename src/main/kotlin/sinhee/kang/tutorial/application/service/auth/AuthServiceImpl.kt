package sinhee.kang.tutorial.application.service.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.application.dto.request.auth.ChangePasswordRequest
import sinhee.kang.tutorial.application.dto.request.auth.SignInRequest
import sinhee.kang.tutorial.application.dto.request.auth.SignUpRequest
import sinhee.kang.tutorial.application.dto.request.auth.VerifyEmailRequest
import sinhee.kang.tutorial.application.dto.response.auth.TokenResponse
import sinhee.kang.tutorial.domain.auth.repository.verification.EmailVerificationRepository
import sinhee.kang.tutorial.application.service.email.EmailService
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.conflict.UserAlreadyExistsException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.forbidden.IncorrectPasswordException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.NotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.UserNotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.UnAuthorizedException
import sinhee.kang.tutorial.application.service.token.JwtTokenProvider
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class AuthServiceImpl(
    private val requestAuthScope: RequestAuthScope,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,

    private val emailService: EmailService,
    private val emailVerificationRepository: EmailVerificationRepository,

    private val userRepository: UserRepository
) : AuthService {

    override fun signIn(signInRequest: SignInRequest, httpServletResponse: HttpServletResponse): TokenResponse {
        val email = signInRequest.email
        val rawPassword = signInRequest.password

        val user = userRepository.findByEmail(email)
            ?.isMatchedPassword(rawPassword)
            ?: throw UserNotFoundException()

        return tokenProvider.setToken(httpServletResponse, user.nickname)
    }

    override fun signUp(signUpRequest: SignUpRequest) {
        val email = signUpRequest.email
        val password = signUpRequest.password
        val nickname = signUpRequest.nickname

        with(userRepository) {
            if (existsByEmail(email) || existsByNickname(nickname))
                throw UserAlreadyExistsException()
        }

        with(emailVerificationRepository) {
            findByEmail(email)
                ?.takeIf { it.isConfirmation() }
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }

        userRepository
            .save(
                signUpRequest
                    .toEntity()
                    .updatePassword(passwordEncoder.encode(password))
            )
            .run { emailService.sendCelebrateEmail(email, nickname) }
    }

    override fun verifyNickname(nickname: String) {
        if (userRepository.existsByNickname(nickname))
            throw UserAlreadyExistsException()
    }

    override fun verifyEmail(verifyEmailRequest: VerifyEmailRequest) {
        val uuid = UUID.fromString(verifyEmailRequest.id)
        val authCode = verifyEmailRequest.authCode

        val confirmationEmail = emailVerificationRepository.findById(uuid)
            .orElseThrow { NotFoundException() }
            .updateAccessTime()

        confirmationEmail
            .takeIf { it.isCorrectAuthCode(authCode) }
            ?.setExpired()
            ?.updateLiveCycle()
            ?: throw PermissionDeniedException()

        emailVerificationRepository.save(confirmationEmail)
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email = changePasswordRequest.email
        val oldPassword = changePasswordRequest.password
        val newPassword = changePasswordRequest.newPassword

        with(emailVerificationRepository) {
            findByEmail(email)
                ?.takeIf { it.isConfirmation() }
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }

        with(userRepository) {
            findByEmail(email)
                ?.isMatchedPassword(oldPassword)
                ?.updatePassword(passwordEncoder.encode(newPassword))
                ?.apply { save(this) }
                ?: throw UnAuthorizedException()
        }
    }

    override fun exitAccount(exitServiceRequest: SignInRequest) {
        val user = requestAuthScope.user
        val email = exitServiceRequest.email
        val rawPassword = exitServiceRequest.password

        with(emailVerificationRepository) {
            findByEmail(email)
                ?.takeIf { it.isConfirmation() }
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }

        with(userRepository) {
            findByEmail(email)
                ?.takeIf { it == user }
                ?.isMatchedPassword(rawPassword)
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }
    }

    override fun extendToken(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): TokenResponse =
        with(tokenProvider) {
            getRefreshToken()
                ?.let { setToken(httpServletResponse, verifyToken(it)) }
                ?: throw BadRequestException()
        }

    private fun User.isMatchedPassword(password: String): User =
        if (!passwordEncoder.matches(password, this.password))
            throw IncorrectPasswordException()
        else this
}
