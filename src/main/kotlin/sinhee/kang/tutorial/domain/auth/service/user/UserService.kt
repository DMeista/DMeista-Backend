package sinhee.kang.tutorial.domain.auth.service.user

import org.springframework.http.HttpStatus
import sinhee.kang.tutorial.domain.auth.dto.request.*

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): HttpStatus
    fun exitAccount(request: ChangePasswordRequest): HttpStatus

    fun isVerifyNickname(nickname: String): HttpStatus
    fun verifyEmail(verifyCodeRequest: VerifyCodeRequest): HttpStatus

    fun changePassword(changePasswordRequest: ChangePasswordRequest): HttpStatus

    fun userAuthenticationSendEmail(sendType: String, emailRequest: EmailRequest): HttpStatus

}
