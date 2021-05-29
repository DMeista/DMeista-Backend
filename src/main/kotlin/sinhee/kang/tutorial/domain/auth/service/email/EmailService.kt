package sinhee.kang.tutorial.domain.auth.service.email

import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.User

interface EmailService {
    fun sendVerificationEmail(emailRequest: EmailRequest, sendType: SendType)

    fun setVerifyEmail(verifyCodeRequest: VerifyCodeRequest)

    fun sendCelebrateEmail(user: User)

    fun String.isVerifyEmail(): String

    fun String.isValidationEmail(): String

    fun String.isExistEmail(sendType: SendType?): String
}
