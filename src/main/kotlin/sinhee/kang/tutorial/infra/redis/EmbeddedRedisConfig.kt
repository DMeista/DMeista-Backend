package sinhee.kang.tutorial.infra.redis

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
@Profile("local")
class EmbeddedRedisConfig {
    private var redisServer: RedisServer? = null

    fun EmbeddedRedisConfig(redisPort: Int = 6379) {
        this.redisServer = RedisServer(redisPort)
    }

    @PostConstruct
    fun runRedis() {
        redisServer?.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer?.stop()
    }
}