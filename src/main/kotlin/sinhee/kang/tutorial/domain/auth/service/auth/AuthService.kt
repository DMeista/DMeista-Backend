package sinhee.kang.tutorial.domain.auth.service.auth

import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.entity.user.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuthService {
    fun signIn(signInRequest: SignInRequest, httpServletResponse: HttpServletResponse): TokenResponse

    fun signUp(signUpRequest: SignUpRequest)

    fun verifyNickname(nickname: String)

    fun verifyEmail(verifyEmailRequest: VerifyEmailRequest)

    fun changePassword(changePasswordRequest: ChangePasswordRequest)

    fun exitAccount(exitServiceRequest: SignInRequest)

    fun extendToken(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): TokenResponse

    fun getCurrentUser(): User
}
