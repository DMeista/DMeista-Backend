package sinhee.kang.tutorial

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test", "local")
class ApiTest