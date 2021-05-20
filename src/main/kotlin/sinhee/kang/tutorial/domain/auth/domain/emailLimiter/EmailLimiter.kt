package sinhee.kang.tutorial.domain.auth.domain.emailLimiter

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.global.businessException.exception.auth.TooManyEmailRequestException

@RedisHash(value = "email_limiter")
data class EmailLimiter(
        @Id
        val email: String,

        var countReq: Int = 0,

        @TimeToLive
        val countTime: Long = 60L
) {
    fun update(): EmailLimiter {
        if (countReq < 10) countReq++
        else throw TooManyEmailRequestException()
        return this
    }
}
