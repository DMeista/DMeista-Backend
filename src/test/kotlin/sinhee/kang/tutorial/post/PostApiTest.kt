package sinhee.kang.tutorial.post

import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test", "local")
class PostApiTest(
        @LocalServerPort
        private var port: Int,
        private var context: WebApplicationContext,
        private var mvc: MockMvc,

        private var userRepository: UserRepository,
        private var postRepository: PostRepository,
        private var passwordEncoder: PasswordEncoder
) {
    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        userRepository.save(User(
                email = "rkdtlsgml50@naver.com",
                password = passwordEncoder.encode("1234"),
                nickname = "sinhee"
        ))
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        postRepository.deleteAll()
    }



}