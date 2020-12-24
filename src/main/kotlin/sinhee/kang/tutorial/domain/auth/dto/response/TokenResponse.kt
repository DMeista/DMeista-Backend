package sinhee.kang.tutorial.domain.auth.dto.response

class TokenResponse(
        var accessToke: String = "",
        var refreshToken: String = "",
        var tokenType: String = ""
)