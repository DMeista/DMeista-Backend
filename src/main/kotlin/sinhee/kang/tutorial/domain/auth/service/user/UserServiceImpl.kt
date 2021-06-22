package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import sinhee.kang.tutorial.domain.auth.repository.verification.AuthVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.validate.ValidateService
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

@Service
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,

    private val authService: AuthService,
    private val validateService: ValidateService,

    private val userRepository: UserRepository,
    private val authVerificationRepository: AuthVerificationRepository
): UserService {

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email = changePasswordRequest.email
        val passwd = changePasswordRequest.password

        validateService.apply {
            validateEmail(email)
            validatePassword(passwd)

            verifiedEmail(email)
        }

        userRepository.apply {
            findByEmail(email)
                ?.apply { password = passwordEncoder.encode(passwd) }
                ?.let { save(it) }
                ?: throw UserNotFoundException()
        }

        authVerificationRepository.deleteById(email)
    }

    override fun exitAccount(request: ChangePasswordRequest) {
        val user = authService.verifyCurrentUser()

        val email = request.email
        val password = request.password

        validateService.apply {
            validateEmail(email)
            validatePassword(password)

            verifiedEmail(email)
        }

        user.isMatchedPassword(passwordEncoder, password)

        authVerificationRepository.deleteById(email)
        userRepository.delete(user)
    }

    override fun isVerifyNickname(verifyNicknameRequest: VerifyNicknameRequest) {
        val email = verifyNicknameRequest.email
        val nickname = verifyNicknameRequest.nickname

        validateService.apply {
            validateEmail(email)
            checkExistNickname(nickname)
        }
        authVerificationRepository.apply {
            val signUpVerification = findById(email)
                .orElseThrow{ UserNotFoundException() }
                .apply { this.nickname = nickname }

            save(signUpVerification)
        }
    }
}
