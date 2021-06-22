package sinhee.kang.tutorial.domain.auth.entity.emailLimiter

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.global.exception.exceptions.tooManyRequests.TooManyEmailRequestsException

@RedisHash(value = "email_limiter")
data class EmailRequestLimiter(
        @Id
        val email: String,

        var countReq: Int = 0,

        @TimeToLive
        val countTime: Long = 60L
) {
    fun update(): EmailRequestLimiter {
        if (countReq < 10) countReq++
        else throw TooManyEmailRequestsException()
        return this
    }
}
