package sinhee.kang.tutorial.domain.auth.service.email

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest
import sinhee.kang.tutorial.domain.auth.entity.requestLimiter.EmailRequestLimiter
import sinhee.kang.tutorial.domain.auth.entity.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.repository.requestLimiter.EmailRequestLimiterRepository
import sinhee.kang.tutorial.domain.auth.repository.verification.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.conflict.UserAlreadyExistsException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException
import sinhee.kang.tutorial.global.exception.exceptions.tooManyRequests.TooManyRequestsException
import java.net.InetAddress
import java.util.*
import kotlin.random.Random

@Service
class EmailServiceImpl(
    @Value("\${spring.mail.username}")
    private val username: String,
    @Value("\${server.port}")
    private val localServerPort: String,

    private val javaMailSender: JavaMailSender,

    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailRequestLimiterRepository: EmailRequestLimiterRepository
): EmailService {

    override fun sendAuthCode(emailRequest: EmailRequest) {
        val email = emailRequest.email
        val uuid = UUID.randomUUID()
        val authCode = generateRandomCode()

        val isUserExist: Boolean = userRepository.existsByEmail(email)
        when (emailRequest.sendType) {
            SendType.USER -> if (!isUserExist) throw UserNotFoundException()
            SendType.REGISTER -> if (isUserExist) throw UserAlreadyExistsException()
        }

        belowRequestLimit(email)

        emailVerificationRepository.save(EmailVerification(uuid, email, authCode))

        sendConfirmationEmail(email, generateConfirmationUrl(uuid, authCode))
    }

    override fun sendCelebrateEmail(email: String, nickname: String) {
        emailSender(
            targetEmail = email,
            subject = "DMeista 가입을 축하합니다!",
            text = "${nickname}님,\nDMeista 서비스 회원가입을 축하합니다."
        )
    }

    private fun sendConfirmationEmail(email: String, url: String) {
        emailSender(
            targetEmail = email,
            subject = "DMeista 인증메일입니다.",
            text = StringBuffer()
                .append("DMeista 서비스 인증 메일입니다.\n")
                .append("${email}님 이메일 주소를 확인하기 위해 아래의 url를 클릭해 주세요.\n\n")
                .append("[ $url ]")
                .toString()
        )
    }

    private fun emailSender(targetEmail: String, subject: String, text: String) {
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

    private fun generateRandomCode(): String {
        val charPool : List<Char> = ('A'..'Z') + ('0'..'9')
        return (0..5)
            .map { Random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun generateConfirmationUrl(uuid: UUID, authCode: String): String {
        val localServerAddress: String = InetAddress.getLocalHost().hostAddress
        val url: StringBuffer = StringBuffer()
            .append("$localServerAddress:$localServerPort")
            .append("/auth/verify")
            .append("?id=$uuid")
            .append("&auth_code=$authCode")

        return url.toString()
    }

    private fun belowRequestLimit(email: String) {
        with(emailRequestLimiterRepository) {
            findById(email)
                .orElseGet { save(EmailRequestLimiter(email)) }
                .apply {
                    if (isNotOver()) save(update())
                    else throw TooManyRequestsException()
                }
        }
    }
}
