package sinhee.kang.tutorial.application.dto.response.auth

data class TokenResponse(
    val tokenType: String = "Bearer",

    val accessToken: String = ""
)
