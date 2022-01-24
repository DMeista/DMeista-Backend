package sinhee.kang.tutorial.application.service.auth

import sinhee.kang.tutorial.application.dto.request.auth.ChangePasswordRequest
import sinhee.kang.tutorial.application.dto.request.auth.SignInRequest
import sinhee.kang.tutorial.application.dto.request.auth.SignUpRequest
import sinhee.kang.tutorial.application.dto.request.auth.VerifyEmailRequest
import sinhee.kang.tutorial.application.dto.response.auth.TokenResponse
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
}
