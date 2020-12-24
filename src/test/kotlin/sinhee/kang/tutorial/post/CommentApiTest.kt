package sinhee.kang.tutorial.post

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommentApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    val testMail = "rkdtlsgml50@naver.com"
    val passwd = "1234"
    val username = "user"

    // TODO: each Test, Before, After temp [Post, User]

    // TODO: Upload Comment, subComment

    // TODO: Change Comment, subComment

    // TODO: Delete Comment, subComment
}