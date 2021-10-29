package sinhee.kang.tutorial.domain.auth.service.email

import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest

interface EmailService {
    fun sendAuthCode(emailRequest: EmailRequest)

    fun sendCelebrateEmail(email: String, nickname: String)
}
