package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.request.SignUpRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
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


    @Before
    fun setup() {
        emailVerificationRepository.save(EmailVerification(
                email = "rkdtlsgml50@naver.com",
                authCode = "ASD123",
                status = EmailVerificationStatus.UNVERIFID
        ))
    }


    @After
    fun clean() {
        emailVerificationRepository.deleteAll()
        userRepository.delete(userRepository.findByNickname("user")!!)
    }


    @Test
    @Throws(Exception::class)
    fun emailVerifyTest() {
        val request = VerifyCodeRequest("rkdtlsgml50@naver.com", "ASD123")
        requestMvc(put("/users/email/verify"), request)
    }


    @Test
    @Throws(Exception::class)
    fun signUpTest() {
        emailVerifyTest()
        val request = SignUpRequest("rkdtlsgml50@naver.com", "1234", "user")
        requestMvc(post("/users"), request)
    }


    @Test
    @Throws(Exception::class)
    fun exitAccountTest() {
        val accessToken = accessKey()
        val request = ChangePasswordRequest("rkdtlsgml50@naver.com", "12345")

        emailVerifyTest()
        mvc.perform(delete("/users")
                .header("Authorization", "Bearer $accessToken")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }


    @Test
    @Throws(Exception::class)
    fun changePasswordTest() {
        val request = ChangePasswordRequest("rkdtlsgml50@naver.com", "12345")

        emailVerifyTest()
        requestMvc(put("/users/password"), request)
    }


    @Test
    @Throws(Exception::class)
    fun nicknameVerifyTest() {
        mvc.perform(get("/users/nickname?nickname=Nickname"))
                .andExpect(status().isOk)
    }


    @Throws(Exception::class)
    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any): MvcResult {
        return mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }


    @Throws(Exception::class)
    private fun signIn(): MvcResult {
        val signInRequest = SignInRequest(email = "rkdtlsgml50@naver.com", password = "1234")
        return requestMvc(post("/auth"), signInRequest)
    }


    private fun accessKey(): String {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToke
    }
}