package sinhee.kang.tutorial.domain.auth.service.user

import sinhee.kang.tutorial.domain.auth.dto.request.*

interface UserService {
    fun signUp(signUpRequest: SignUpRequest)
    fun exitAccount(request: ChangePasswordRequest)

    fun isVerifyNickname(nickname: String): Boolean
    fun verifyEmail(verifyCodeRequest: VerifyCodeRequest)

    fun changePassword(changePasswordRequest: ChangePasswordRequest)
    fun changeEmail(changeEmailRequest: ChangeEmailRequest)

    fun userAuthenticationSendEmail(sendType: String, emailRequest: EmailRequest)

}