package sinhee.kang.tutorial.application.service.email

import sinhee.kang.tutorial.application.dto.request.auth.EmailRequest

interface EmailService {
    fun sendAuthCode(emailRequest: EmailRequest)

    fun sendCelebrateEmail(email: String, nickname: String)
}
