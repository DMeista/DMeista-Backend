package sinhee.kang.tutorial.domain.auth.service.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EmailServiceImpl(
        @Autowired
        var javaMailSender: JavaMailSender
) : EmailService {
    override fun sendEmail(email: String, code: String) {
        val msg = SimpleMailMessage()
        msg.setTo(email)
        msg.setFrom("kangsinhee40@gmail.com")
        msg.setSubject("Spring boot Tutorial 메일")
        msg.setText("${LocalDateTime.now()}\n code: $code")

        javaMailSender.send(msg)
    }
}