package sinhee.kang.tutorial.global.security.jwt.enums

enum class TokenType(val tokenName: String, val expiredTokenTime: Long) {
    ACCESS("access_token", 60L * 60L * 2L),
    REFRESH("_Refresh", 60L * 60L * 24L * 7L)
}
