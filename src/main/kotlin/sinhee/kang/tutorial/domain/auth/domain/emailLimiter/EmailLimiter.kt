package sinhee.kang.tutorial.domain.auth.domain.emailLimiter

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "email_limiter")
data class EmailLimiter(
        @Id
        val email: String = "",

        @TimeToLive
        var count: Long = 0
) {
    fun updateCount(): EmailLimiter {
        if (isBelowLimit())
            count += 6
        return this
    }

    fun isBelowLimit(): Boolean =
        count <= 60
}
