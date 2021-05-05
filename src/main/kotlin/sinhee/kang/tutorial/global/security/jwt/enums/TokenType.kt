package sinhee.kang.tutorial.global.security.jwt.enums

enum class TokenType(var tokenName: String, var cookieName: String, var expiredTokenTime: Long) {
    ACCESS("access_token", "_Access", 60 * 60 * 2),
    REFRESH("refresh_token", "_Refresh", 60 * 60 * 24 * 7)
}
