package sinhee.kang.tutorial.domain.auth.dto.response

data class TokenResponse(
        val tokenType: String = "Bearer",

        val accessToken: String
)
