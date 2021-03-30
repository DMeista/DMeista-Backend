package sinhee.kang.tutorial.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurerSupport() {
    override fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor()
        .apply {
            corePoolSize = 2
            maxPoolSize = 10
            setQueueCapacity(500)
            setThreadNamePrefix("async")
            initialize() }
}