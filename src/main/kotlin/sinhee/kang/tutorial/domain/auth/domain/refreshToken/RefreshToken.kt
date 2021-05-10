package sinhee.kang.tutorial.domain.auth.domain.refreshToken

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refresh_token")
class RefreshToken(
        @Id
        val nickname: String,

        @Indexed
        val refreshToken: String,

        @TimeToLive
        val ttl: Long = 0L
)
