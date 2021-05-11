package sinhee.kang.tutorial.domain.auth.service.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.repository.EmailLimiterRepository
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidAuthCodeException
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidAuthEmailException
import sinhee.kang.tutorial.global.businessException.exception.auth.TooManyEmailRequestException
import sinhee.kang.tutorial.global.businessException.exception.auth.UserAlreadyExistsException
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException
import kotlin.random.Random

@Service
class EmailServiceImpl(
    @Value("\${spring.mail.username}")
    private val username: String,

    private val javaMailSender: JavaMailSender,

    private val userRepository: UserRepository,
    private val emailLimiterRepository: EmailLimiterRepository,
    private val emailVerificationRepository: EmailVerificationRepository
) : EmailService {

    override fun sendVerificationEmail(emailRequest: EmailRequest, sendType: String) {
        val email = emailRequest.email
            .apply {
                checkedEmailExist(sendType)
                isBelowRequestLimit()
            }
        val randomCode: String = generateRandomCode()

        sendVerifyEmail(email, randomCode)
    }

    override fun verifyEmail(verifyCodeRequest: VerifyCodeRequest) {
        val email: String = verifyCodeRequest.email
        val code: String = verifyCodeRequest.authCode

        val emailVerification = emailVerificationRepository.findById(email)
            .orElseThrow { InvalidAuthEmailException() }

        if (emailVerification.authCode != code)
            throw InvalidAuthCodeException()

        emailVerificationRepository.save(emailVerification.verify())
    }

    @Async
    override fun sendCelebrateEmail(user: User) =
        sendEmail(
            targetEmail = user.email,
            subject = "DMeista 가입을 축하합니다!",
            text = "${user.nickname}님,\nDMeista 서비스 회원가입을 축하합니다."
        )

    @Async
    fun sendVerifyEmail(email: String, code: String) {
        emailVerificationRepository.save(EmailVerification(email, code))
        sendEmail(
            targetEmail = email,
            subject = "DMeista 인증메일입니다.",
            text = "DMeista 서비스 인증 메일입니다." +
                    "DMeista에 가입하신 것을 환영합니다." +
                    "${email}님 이메일 주소를 확인하기 위해 아래의 코드를 입력해주세요." +
                    "" +
                    "[ $code ]"
        )
    }

    fun sendEmail(targetEmail: String, subject: String, text: String) {
        SimpleMailMessage()
            .apply {
                setFrom(username)
                setTo(targetEmail)
                setSubject(subject)
                setText(text)
            }
            .let { javaMailSender.send(it) }
    }

    private fun String.checkedEmailExist(sendType: String?) {
        val user = userRepository.findByEmail(this)
        when(sendType) {
            "signup" -> user?.let { throw UserAlreadyExistsException() }
            "user" -> user ?: throw UserNotFoundException()
        }
    }

    private fun String.isBelowRequestLimit() =
        emailLimiterRepository.findById(this)
            .orElseGet{ emailLimiterRepository.save(EmailLimiter(this)) }
            .let { limit -> emailLimiterRepository.save(limit.updateCount()) }
            .takeIf { !it.isBelowLimit() }
            ?.also { throw TooManyEmailRequestException() }

    private fun generateRandomCode(): String {
        val charPool : List<Char> = ('A'..'Z') + ('0'..'9')
        return (0..4)
            .map { Random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
