package sinhee.kang.tutorial.domain.auth.dto.response

class TokenResponse(
        var accessToken: String = "",
        var refreshToken: String = "",
        var tokenType: String = ""
)