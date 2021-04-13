package sinhee.kang.tutorial.domain.auth.service.auth

import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.User

interface AuthService {
    fun signIn(request: SignInRequest): TokenResponse
    fun refreshToken(refreshToken: String): TokenResponse

    fun authValidate(): User
}
