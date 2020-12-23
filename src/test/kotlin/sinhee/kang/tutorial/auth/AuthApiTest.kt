package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
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
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AuthApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    @Before
    fun setup() {
        userRepository.save(User(
                email = "rkdtlsgml50@naver.com",
                password = passwordEncoder.encode("1234"),
                nickname = "user"
        ))
    }


    @After
    fun clean() {
        userRepository.delete(userRepository.findByNickname("user")!!)
    }


    @Test
    @Throws(Exception::class)
    fun signInTest() {
        signIn()
    }


    @Test
    @Throws(Exception::class)
     fun refreshTokenTest() {
        val refreshToken = refreshKey()

        mvc.perform(put("/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
     }


    @Throws(Exception::class)
    private fun signIn(): MvcResult {
        val signInRequest = SignInRequest(email = "rkdtlsgml50@naver.com", password = "1234")
        return mvc.perform(post("/auth")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }


    private fun refreshKey(): String {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.refreshToken
    }
}