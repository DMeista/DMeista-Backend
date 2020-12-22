package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.Test
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Tag("First")
    @Test
    @Throws(Exception::class)
    fun emailVerifyTest() {
        val request = VerifyCodeRequest("rkdtlsgml50@naver.com", "ASD123")
        requestMvc(MockMvcRequestBuilders.post("/users/email/verify/signup"), request)

    }


    @Throws(Exception::class)
    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any) {
        mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
    }
}