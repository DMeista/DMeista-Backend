package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test", "local")
class AuthApiTest(
        @LocalServerPort
        private var port: Int,
        private var context: WebApplicationContext,
        private var mvc: MockMvc,

        private var userRepository: UserRepository,
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
    }

    @Tag("First")
    @Test
    @Throws(Exception::class)
    fun signInTest() {
        signIn()
    }

    @Tag("First")
    @Test
    @Throws(Exception::class)
    fun refreshTest() {
        val url = "http://localhost: $port"

        val content: String = signIn().response.contentAsString
        val response: TokenResponse? = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        val refreshToken: String = response!!.refreshToken

        mvc.perform(put("$url/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk)
    }

    @Throws(Exception::class)
    fun signIn(): MvcResult {
        val url = "http://localhost: $port"

        val signInRequest = SignInRequest(email = "rkdtlsgml50@naver.com", password = "1234")
        return mvc.perform(post("$url/auth")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }
}