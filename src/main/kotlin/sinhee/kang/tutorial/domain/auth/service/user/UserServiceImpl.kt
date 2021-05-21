package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.*
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

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
            .apply { isVerifyEmail() }

        userRepository.apply {
            findByEmail(email)
                ?.let { throw UserAlreadyExistsException() }
            save(signUpRequest.toEntity(passwordEncoder))
                .also { publisher.publishEvent(emailService.sendCelebrateEmail(it)) }
        }
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email: String = changePasswordRequest.email
            .apply { isVerifyEmail() }

        userRepository.findByEmail(email)
            ?.apply { password = passwordEncoder.encode(changePasswordRequest.password) }
            ?.let { userRepository.save(it) }
            ?: throw UserNotFoundException()

        emailVerificationRepository.deleteById(email)
    }

    override fun exitAccount(request: ChangePasswordRequest) {
        val user = authService.authValidate()
        val email = request.email
            .apply { isVerifyEmail() }

        if (!passwordEncoder.matches(request.password, user.password))
            throw UnAuthorizedException()

        emailVerificationRepository.deleteById(email)
        userRepository.delete(user)
    }

    override fun isVerifyNickname(nickname: String) {
        userRepository.findByNickname(nickname)
            ?.let { throw UserAlreadyExistsException() }
    }

    private fun String.isVerifyEmail() =
        emailVerificationRepository.findById(this)
            .filter(EmailVerification::isVerify)
            .orElseThrow{ ExpiredAuthCodeException() }
}
