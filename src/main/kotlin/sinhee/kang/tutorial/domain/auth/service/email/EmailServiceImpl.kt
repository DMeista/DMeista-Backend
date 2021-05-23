package sinhee.kang.tutorial.domain.auth.service.email

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.repository.EmailLimiterRepository
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
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

    override fun verifyEmail(verifyCodeRequest: VerifyCodeRequest) {
        val email: String = verifyCodeRequest.email
        val code: String = verifyCodeRequest.authCode

        emailVerificationRepository.apply {
            findById(email)
                .orElseThrow { UserNotFoundException() }
                .checkAuthCode(code)
                .also { save(it.setVerify()) }
        }
    }

    override fun sendVerificationEmail(emailRequest: EmailRequest, sendType: SendType) {
        val email = emailRequest.email.apply {
            checkedEmailExist(sendType)
            belowRequestLimit()
        }
        sendVerifyEmailFactory(email)
    }

    override fun sendCelebrateEmail(user: User) {
        val email = user.email
        val nickname = user.nickname

        sendEmail(
            targetEmail = email,
            subject = "DMeista 가입을 축하합니다!",
            text = "${nickname}님,\nDMeista 서비스 회원가입을 축하합니다."
        )
    }

    private fun sendVerifyEmailFactory(email: String) {
        val randomCode = generateRandomCode()
        emailVerificationRepository.save(EmailVerification(email, randomCode))

        sendEmail(
            targetEmail = email,
            subject = "DMeista 인증메일입니다.",
            text = "DMeista 서비스 인증 메일입니다.\n" +
                    "DMeista에 가입하신 것을 환영합니다.\n" +
                    "${email}님 이메일 주소를 확인하기 위해 아래의 코드를 입력해주세요.\n\n" +
                    "[ $randomCode ]"
        )
    }

    private fun sendEmail(targetEmail: String, subject: String, text: String) {
        GlobalScope.launch {
            javaMailSender.send(
                SimpleMailMessage().apply {
                    setFrom(username)
                    setTo(targetEmail)
                    setSubject(subject)
                    setText(text)
                }
            )
        }
    }

    private fun String.checkedEmailExist(sendType: SendType?) {
        val user = userRepository.findByEmail(this)
        when(sendType) {
            SendType.REGISTER -> user
                ?.let { throw UserAlreadyExistsException() }
            SendType.USER -> user
                ?: throw UserNotFoundException()
        }
    }

    private fun String.belowRequestLimit() {
        emailLimiterRepository.findById(this)
            .orElseGet { emailLimiterRepository.save(EmailLimiter(this)) }
            .apply { emailLimiterRepository.save(update()) }
    }

    private fun generateRandomCode(): String {
        val charPool : List<Char> = ('A'..'Z') + ('0'..'9')
        return (0..4)
            .map { Random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
