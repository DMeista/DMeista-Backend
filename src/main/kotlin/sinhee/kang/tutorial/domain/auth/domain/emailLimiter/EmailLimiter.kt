package sinhee.kang.tutorial.domain.auth.domain.emailLimiter

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "email_limiter")
class EmailLimiter(
        @Id
        var email: String = "",

        @TimeToLive
        var count: Long
) {
    fun update(limit: Int): EmailLimiter {
        if (isBelowLimit()) {
            count += limit
        }
        return this
    }

    fun isBelowLimit(): Boolean {
        return count <= 60
    }
}
