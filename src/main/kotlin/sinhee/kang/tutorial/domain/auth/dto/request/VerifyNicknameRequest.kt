package sinhee.kang.tutorial.domain.auth.dto.request

data class VerifyNicknameRequest(
    val email: String,

    val nickname: String
)
