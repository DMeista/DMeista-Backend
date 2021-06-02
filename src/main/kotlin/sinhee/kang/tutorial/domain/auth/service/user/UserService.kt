package sinhee.kang.tutorial.domain.auth.service.user

import sinhee.kang.tutorial.domain.auth.dto.request.*

interface UserService {
    fun signUp(signUpRequest: SignUpRequest)

    fun changePassword(changePasswordRequest: ChangePasswordRequest)

    fun exitAccount(request: ChangePasswordRequest)

    fun isVerifyNickname(verifyNicknameRequest: VerifyNicknameRequest)
}
