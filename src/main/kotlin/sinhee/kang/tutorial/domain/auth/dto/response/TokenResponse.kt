package sinhee.kang.tutorial.domain.auth.dto.response

data class TokenResponse (
        var accessToken: String = "",
        var tokenType: String = "Bearer"
)
