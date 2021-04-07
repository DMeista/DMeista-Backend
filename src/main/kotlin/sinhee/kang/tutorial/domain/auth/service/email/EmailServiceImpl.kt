package sinhee.kang.tutorial.domain.auth.service.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import sinhee.kang.tutorial.domain.user.domain.user.User

@Component
class EmailServiceImpl(
        private val javaMailSender: JavaMailSender
) : EmailService {
    private val msg: SimpleMailMessage = SimpleMailMessage()

    init {
        msg.setFrom("kangsinhee40@gmail.com")
    }


    @Async
    override fun sendVerifyEmail(email: String, code: String) {
        msg.run {
            setTo(email)
            setSubject("DMeista 인증메일입니다.")
            setText("<h2>DMeista 서비스 인증 메일입니다.</h2> \n\n[ $code ]")
        }

        javaMailSender.send(msg)
    }

    @Async
    override fun sendCelebrateEmail(user: User) {
        msg.run {
            setTo(user.email)
            setSubject("DMeista 가입을 축하합니다!")
            setText("${user.nickname}님,\nDMeista 서비스 회원가입을 축하합니다.")
        }

        javaMailSender.send(msg)
    }
}