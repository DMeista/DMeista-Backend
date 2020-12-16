package sinhee.kang.tutorial.infra.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


@Configuration
class EmailConfig(
        @Value("\${spring.mail.host}")
        private val host: String,

        @Value("\${spring.mail.port}")
        private val port: Int
) {
    @Bean
    fun javaMailService(): JavaMailSender? {
        val javaMailSender = JavaMailSenderImpl()
        javaMailSender.host = host
        javaMailSender.port = port
        javaMailSender.javaMailProperties = getMailProperties()
        return javaMailSender
    }

    private fun getMailProperties(): Properties {
        val properties = Properties()
        properties.setProperty("mail.transport.protocol", "smtp")
        properties.setProperty("mail.smtp.auth", "false")
        properties.setProperty("mail.smtp.starttls.enable", "false")
        properties.setProperty("mail.debug", "false")
        return properties
    }
}