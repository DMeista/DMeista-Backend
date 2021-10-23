package sinhee.kang.tutorial.domain.auth.service.auth

import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.auth.repository.verification.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.UnAuthorizedException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.global.exception.exceptions.conflict.UserAlreadyExistsException
import sinhee.kang.tutorial.global.exception.exceptions.forbidden.IncorrectPasswordException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.NotFoundException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.global.security.authentication.AuthenticationService
import sinhee.kang.tutorial.global.security.jwt.JwtTokenProvider
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val authenticationService: AuthenticationService,

    private val emailService: EmailService,
    private val emailVerificationRepository: EmailVerificationRepository,

    private val userRepository: UserRepository,
): AuthService {

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
            .save(signUpRequest
                .toEntity()
                .updatePassword(passwordEncoder.encode(password)))
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
        val user = getCurrentUser()
        val email = exitServiceRequest.email
        val password = exitServiceRequest.password

        with(emailVerificationRepository) {
            findByEmail(email)
                ?.takeIf { it.isConfirmation() }
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }

        with(userRepository) {
            findByEmail(email)
                ?.takeIf { it == user }
                ?.isMatchedPassword(password)
                ?.apply { delete(this) }
                ?: throw UnAuthorizedException()
        }
    }

    override fun extendToken(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): TokenResponse =
        with(tokenProvider) {
            getRefreshToken(httpServletRequest)
            ?.takeIf { isValidateToken(it) && isRefreshToken(it) }
            ?.let { setToken(httpServletResponse, getUsername(it)) }
            ?: throw BadRequestException()
        }

    override fun getCurrentUser(): User {
        val currentUsername = authenticationService.getUserName()

        return userRepository.findByNickname(currentUsername)
            ?: throw UnAuthorizedException()
    }

    private fun User.isMatchedPassword(password: String): User =
        if (!passwordEncoder.matches(password, this.password))
            throw IncorrectPasswordException()
        else this
}
