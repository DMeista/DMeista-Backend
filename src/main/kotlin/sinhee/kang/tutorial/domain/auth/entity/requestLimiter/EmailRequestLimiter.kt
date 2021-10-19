package sinhee.kang.tutorial.domain.auth.entity.requestLimiter

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.global.exception.exceptions.tooManyRequests.TooManyEmailRequestsException

@RedisHash
class EmailRequestLimiter(
    @Id
    val email: String,

    private var count: Int = 0,

    @TimeToLive
    val countTime: Long = 60L
) {
    fun isNotOver(): Boolean =
        count >= 5

    fun update(): EmailRequestLimiter =
        apply { count++ }
}
