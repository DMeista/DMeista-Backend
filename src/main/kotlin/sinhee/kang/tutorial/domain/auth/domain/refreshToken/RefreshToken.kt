package sinhee.kang.tutorial.domain.auth.domain.refreshToken

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refresh_token")
class RefreshToken(
        @Id
        var nickname: String,

        @Indexed
        var refreshToken: String,

        @TimeToLive
        var ttl: Long = 0L
) {
    fun update(refreshToken: String, ttl: Long): RefreshToken {
        this.refreshToken = refreshToken
        this.ttl = ttl
        return this
    }
}