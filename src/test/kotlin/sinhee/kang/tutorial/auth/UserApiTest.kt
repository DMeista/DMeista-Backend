package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
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
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    var email: String = "rkdtlsgml50@naver.com"
    var password: String = "1234"

    @Before
    fun setup() {
        emailVerificationRepository.save(EmailVerification(
                email = email,
                authCode = "ASD123",
                status = EmailVerificationStatus.UNVERIFID
        ))
    }


    @Test
    @Throws
    fun T1_signUpTest() {
        emailVerifyTest(email)
        val request = SignUpRequest(email, password, "user")
        requestMvc(post("/users"), request)
    }


    @Test
    @Throws
    fun T2_nicknameVerifyTest() {
        mvc.perform(get("/users/nickname?nickname=user"))
                .andExpect(status().isOk)
    }


    @Test
    @Throws
    fun T3_changePasswordTest() {
        val request = ChangePasswordRequest(email, "12345")
        emailVerifyTest(email)
        requestMvc(put("/users/password"), request)
    }


    @Test
    fun T4_exitAccountTest() {
        password = "12345"
        emailVerifyTest(email)
        val accessToken = accessKey()
        val request = ChangePasswordRequest(email, password)

        mvc.perform(delete("/users")
                .header("Authorization", "Bearer $accessToken")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk).andDo(print())
                .andReturn()
    }


    @Throws
    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any) {
        mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn()
    }


    private fun emailVerifyTest(email: String) {
        val request = VerifyCodeRequest(email, "ASD123")
        requestMvc(put("/users/email/verify"), request)
    }


    @Throws
    private fun signIn(): MvcResult {
        val signInRequest = SignInRequest("rkdtlsgml50@naver.com", "12345")
        return mvc.perform(post("/auth")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn()
    }

    private fun accessKey(): String {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToke
    }
}