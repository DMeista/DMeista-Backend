package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.verification.SignUpVerification

import sinhee.kang.tutorial.domain.auth.domain.verification.repository.SignUpVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import sinhee.kang.tutorial.domain.auth.service.validate.ValidateService
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidAuthEmailException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

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
        val password = signUpRequest.password
        val nickname = signUpRequest.nickname

        validateService.apply {
            validateEmail(email)
            validatePassword(password)
            validateNickname(nickname)

            validateExistEmail(email, SendType.REGISTER)
        }
        with(email) {
            validateVerifiedEmail()
            validateVerifiedNickname(nickname)
        }

        userRepository.save(signUpRequest.toEntity(passwordEncoder))
            .also { emailService.sendCelebrateEmail(it) }

        signUpVerificationRepository.deleteById(email)
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email = changePasswordRequest.email
        val passwd = changePasswordRequest.password

        validateService.apply {
            validateEmail(email)
            validatePassword(passwd)
        }
        email.validateVerifiedEmail()

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
            validatePassword(password)
        }
        email.validateVerifiedEmail()

        user.isMatchedPassword(passwordEncoder, password)

        signUpVerificationRepository.deleteById(email)
        userRepository.delete(user)
    }

    override fun isVerifyNickname(verifyNicknameRequest: VerifyNicknameRequest) {
        val email = verifyNicknameRequest.email
        val nickname = verifyNicknameRequest.nickname

        validateService.apply {
            validateEmail(email)
            validateNickname(nickname)
        }
        signUpVerificationRepository.apply {
            val signUpVerification = findById(email)
                .orElseThrow{ UserNotFoundException() }
                .apply { this.nickname = nickname }

            save(signUpVerification)
        }
    }

    private fun String.validateVerifiedEmail() {
        signUpVerificationRepository.findById(this)
            .filter(SignUpVerification::isVerify)
            .orElseThrow { InvalidAuthEmailException() }
    }

    private fun String.validateVerifiedNickname(nickname: String) {
        signUpVerificationRepository.findById(this)
            .orElseThrow { BadRequestException() }
            .checkEqualNickname(nickname)
    }
}
