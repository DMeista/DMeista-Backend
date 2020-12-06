package sinhee.kang.tutorial.infra.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import java.time.Duration

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
class RedisRepository {
    var redisHost: String = "localhost"
    var redisPort: Int = 6379
    var password: String = ""

    @Bean()
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val redisConfig: RedisStandaloneConfiguration = RedisStandaloneConfiguration(redisHost, redisPort)
        if (!password.isBlank())
            redisConfig.setPassword(password)
        val clientConfig: LettuceClientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(1))
                .shutdownTimeout(Duration.ZERO)
                .build()
        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())
        return redisTemplate
    }
}