package sinhee.kang.tutorial.domain.auth.service.auth

import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AuthService {
    fun signIn(signInRequest: SignInRequest, httpServletResponse: HttpServletResponse): TokenResponse

    fun extendAuthTokens(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): TokenResponse

    fun verifyCurrentUser(): User
}
