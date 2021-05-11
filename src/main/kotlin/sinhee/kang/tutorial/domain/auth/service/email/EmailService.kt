package sinhee.kang.tutorial.domain.auth.service.email

import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.User

interface EmailService {
    fun sendVerificationEmail(emailRequest: EmailRequest, sendType: String)

    fun verifyEmail(verifyCodeRequest: VerifyCodeRequest)

    fun sendCelebrateEmail(user: User)
}
