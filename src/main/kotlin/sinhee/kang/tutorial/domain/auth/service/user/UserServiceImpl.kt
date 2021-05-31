package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.domain.verification.repository.SignUpVerificationRepository
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
    private val signUpVerificationRepository: SignUpVerificationRepository
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
        signUpVerificationRepository.deleteById(email)
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

        signUpVerificationRepository.deleteById(email)
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

        user.isMatchedPassword(passwordEncoder, password)

        signUpVerificationRepository.deleteById(email)
        userRepository.delete(user)
    }

    override fun isVerifyNickname(nickname: String) {
        userRepository.findByNickname(nickname)
            ?.let { throw UserAlreadyExistsException() }
    }
}
