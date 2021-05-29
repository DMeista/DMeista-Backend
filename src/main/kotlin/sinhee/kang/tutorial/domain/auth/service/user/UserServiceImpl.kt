package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.SendType
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.*
import sinhee.kang.tutorial.global.businessException.exception.common.BadRequestException
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

import java.util.regex.Pattern

@Service
class UserServiceImpl(
    private val publisher: ApplicationEventPublisher,
    private val passwordEncoder: PasswordEncoder,

    private val authService: AuthService,
    private val emailService: EmailService,

    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository
): UserService {

    override fun signUp(signUpRequest: SignUpRequest) {
        val email = signUpRequest.email
        signUpRequest.password
            .isValidPassword()

        emailService.apply {
            email.apply {
                isValidationEmail()
                isVerifyEmail()
                isExistEmail(SendType.REGISTER)
            }
        }

        userRepository.apply {
            val user = signUpRequest.toEntity(passwordEncoder)

            save(user).also {
                publisher.publishEvent(emailService.sendCelebrateEmail(it))
            }
        }
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email: String = changePasswordRequest.email
        val passwd = changePasswordRequest.password
            .isValidPassword()

        emailService.apply {
            email.apply {
                isValidationEmail()
                isVerifyEmail()
            }
        }

        userRepository.apply {
            findByEmail(email)
                ?.apply { password = passwordEncoder.encode(passwd) }
                ?.let { save(it) }
                ?: throw UserNotFoundException()
        }

        emailVerificationRepository.deleteById(email)
    }

    override fun exitAccount(request: ChangePasswordRequest) {
        val user = authService.verifyCurrentUser()

        val email = request.email
        val password = request.password
            .isValidPassword()

        emailService.apply {
            email.apply {
                isValidationEmail()
                isVerifyEmail()
            }
        }

        if (!passwordEncoder.matches(password, user.password))
            throw UnAuthorizedException()

        emailVerificationRepository.deleteById(email)
        userRepository.delete(user)
    }

    override fun isVerifyNickname(nickname: String) {
        userRepository.findByNickname(nickname)
            ?.let { throw UserAlreadyExistsException() }
    }

    private fun String.isValidPassword(): String {
        Pattern
            .compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}")
            .matcher(this)
            .matches()
            .takeIf { !it }
            ?: throw BadRequestException()
        return this
    }
}
