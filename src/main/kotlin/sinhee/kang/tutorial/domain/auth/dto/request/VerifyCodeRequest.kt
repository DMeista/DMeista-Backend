package sinhee.kang.tutorial.domain.auth.dto.request

data class VerifyCodeRequest(
    val email: String,

    val authCode: String
)
