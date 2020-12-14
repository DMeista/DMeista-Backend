package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.repository.EmailLimiterRepository
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.exception.ExpiredAuthCodeException
import sinhee.kang.tutorial.domain.auth.exception.InvalidAuthCodeException
import sinhee.kang.tutorial.domain.auth.exception.InvalidAuthEmailException
import sinhee.kang.tutorial.domain.auth.exception.UserAlreadyExistsException
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.post.exception.UnAuthorizedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.service.friend.FriendService
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import java.time.LocalDateTime
import java.util.*

@Service
class UserServiceImpl(
        private var userRepository: UserRepository,

        private var friendService: FriendService,
        private var authService: AuthService,

        private var emailVerificationRepository: EmailVerificationRepository,
        private var emailLimiterRepository: EmailLimiterRepository,
        private var emailService: EmailService,
        private var passwordEncoder: PasswordEncoder
) : UserService {

    private var limit: Int = 6

    override fun signUp(signUpRequest: SignUpRequest) {
        val email: String = signUpRequest.email
        val password = passwordEncoder.encode(signUpRequest.password)
        val nickname: String = signUpRequest.nickname

//        val emailVerification: EmailVerification = emailVerificationRepository.findById(email)
//                .filter(EmailVerification::isVerify)
//                .orElseThrow { ExpiredAuthCodeException() }

        userRepository.findByEmail(email)
                ?.let { throw UserAlreadyExistsException() }

        userRepository.save(User(
                email = email,
                password = password,
                nickname = nickname,
                roles = AccountRole.USER,
                createdAt = LocalDateTime.now()
        ))

//        emailVerificationRepository.save(emailVerification.setUnVerify())
    }


    override fun exitAccount(request: ChangePasswordRequest) {
        val user = authService.authValidate()
//        val email = request.email
//        val emailVerification: EmailVerification = emailVerificationRepository.findById(email)
//                .filter(EmailVerification::isVerify)
//                .orElseThrow { ExpiredAuthCodeException() }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw UnAuthorizedException()
        }
        userRepository.delete(user)
//        emailVerificationRepository.save(emailVerification.setUnVerify())
    }


    override fun verifyEmail(verifyCodeRequest: VerifyCodeRequest) {
        val email: String = verifyCodeRequest.email
        val code: String = verifyCodeRequest.authCode
        val emailVerification: EmailVerification = emailVerificationRepository.findById(email)
                .orElseThrow { InvalidAuthEmailException() }

        if (emailVerification.authCode != code) {
            throw InvalidAuthCodeException()
        }

        emailVerification.verify()
        emailVerificationRepository.save(emailVerification)
    }


    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val email: String = changePasswordRequest.email
        val password: String = passwordEncoder.encode(changePasswordRequest.password)

        val emailVerification: EmailVerification = emailVerificationRepository.findById(email)
                .filter(EmailVerification::isVerify)
                .orElseThrow { ExpiredAuthCodeException() }

        val user: User = userRepository.findByEmail(email)
                ?: { throw UserNotFoundException() }()
        user.password = password
        userRepository.save(user)

        emailVerificationRepository.save(emailVerification.setUnVerify())
    }


    override fun changeEmail(changeEmailRequest: ChangeEmailRequest) {
        val email: String = changeEmailRequest.beforeEmail
        val newEmail: String = changeEmailRequest.afterEmail

        val emailVerification: EmailVerification = emailVerificationRepository.findById(email)
                .filter(EmailVerification::isVerify)
                .orElseThrow { ExpiredAuthCodeException() }

        val user: User = userRepository.findByEmail(email)
                ?: { throw UserNotFoundException() }()
        userRepository.findByEmail(newEmail)
                ?.let { throw UserAlreadyExistsException() }
        user.email = newEmail
        userRepository.save(user)

        emailVerificationRepository.save(emailVerification.setUnVerify())
    }


    override fun userAuthenticationSendEmail(sendType: String, emailRequest: EmailRequest) {
        val email: String = emailRequest.email
        userRepository.findByEmail(email)
                .emailTypeAction(sendType)

        if (!isUnderRequestLimit(email)) throw TooManyListenersException()

        val code = randomCode()
        emailService.sendEmail(email, code)
        emailVerificationRepository.save(EmailVerification(
                email = email,
                authCode = code
        ))
    }


    private fun randomCode(): String {
        val result = StringBuilder()
        val codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("").toTypedArray()
        for (i in 0..5) {
            result.append(codes[Random().nextInt(codes.size)])
        }
        return result.toString()
    }


    private fun isUnderRequestLimit(email: String): Boolean {
        return emailLimiterRepository.findById(email)
                .let { Optional.of(EmailLimiter(email, 0L)) }
                .map { limiter -> emailLimiterRepository.save(limiter.update(limit)) }
                .map(EmailLimiter::isBelowLimit)
                .get()
    }


    private fun User?.emailTypeAction(sendType: String?) {
        when(sendType) {
            "signup" -> this?.let { throw UserAlreadyExistsException() }
            "user" -> this ?: { throw UserNotFoundException() }()
        }
    }
}