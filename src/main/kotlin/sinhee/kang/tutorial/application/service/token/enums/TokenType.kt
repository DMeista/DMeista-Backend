package sinhee.kang.tutorial.application.service.token.enums

enum class TokenType(val tokenName: String, val expiredTokenTime: Long) {
    ACCESS("access_token", 60L * 60L * 2L * 1000),
    REFRESH("_Refresh", 60L * 60L * 24L * 7L * 1000)
}
