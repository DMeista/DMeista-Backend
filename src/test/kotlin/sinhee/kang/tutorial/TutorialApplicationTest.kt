package sinhee.kang.tutorial

import org.junit.Test
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(
        classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TutorialApplicationTests {
    private lateinit var mvc: MockMvc

    @Autowired
    private var context: WebApplicationContext? = null
    @Autowired
    private var userRepository: UserRepository? = null
    @Autowired
    private var passwordEncoder: PasswordEncoder? = null

    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context!!)
                .build()
        userRepository!!.save(User(
                email = "rkdtlsgml50@naver.com",
                password = passwordEncoder!!.encode("1234"),
                nickname = "sinhee"
        ))
    }
    @AfterEach
    fun clean() {
        userRepository!!.deleteAll()
    }

    @Test
    fun contextLoads() {
    }
}