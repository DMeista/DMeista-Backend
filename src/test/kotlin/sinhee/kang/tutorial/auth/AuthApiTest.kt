package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.Test
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AuthApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @Throws(Exception::class)
     fun refreshTokenTest() {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        val refreshToken: String = response.refreshToken

        mvc.perform(put("/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
     }


    @Test
    @Throws(Exception::class)
    fun signInTest() {
        signIn()
    }


    fun signIn(): MvcResult {
        val signInRequest = SignInRequest(email = "rkdtlsgml50@naver.com", password = "1234")
        return mvc.perform(post("/auth")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }
}