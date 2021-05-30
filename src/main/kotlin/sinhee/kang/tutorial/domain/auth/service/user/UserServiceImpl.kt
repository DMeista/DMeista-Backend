package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.SendType
import sinhee.kang.tutorial.domain.auth.service.validate.ValidateService
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.*
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

@Service
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,

    private val authService: AuthService,
    private val emailService: EmailService,
    private val validateService: ValidateService,

    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository
): UserService {

    override fun signUp(signUpRequest: SignUpRequest) {
        val email = signUpRequest.email

        validateService.apply {
            validateEmail(email)
            validateExistEmail(email, SendType.REGISTER)
            validatePassword(signUpRequest.password)
        }

        userRepository.save(signUpRequest.toEntity(passwordEncoder))
            .let { emailService.sendCelebrateEmail(it) }
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email = changePasswordRequest.email
        val passwd = changePasswordRequest.password

        validateService.apply {
            validateEmail(email)
            validateVerifiedEmail(email)
            validatePassword(passwd)
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

        validateService.apply {
            validateEmail(email)
            validateVerifiedEmail(email)
            validatePassword(password)
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
}
