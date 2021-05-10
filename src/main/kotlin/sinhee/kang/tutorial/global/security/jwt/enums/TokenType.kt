package sinhee.kang.tutorial.global.security.jwt.enums

enum class TokenType(val tokenName: String, val expiredTokenTime: Long) {
    ACCESS("access_token", 60 * 60 * 2),
    REFRESH("_Refresh", 60 * 60 * 24 * 7)
}
