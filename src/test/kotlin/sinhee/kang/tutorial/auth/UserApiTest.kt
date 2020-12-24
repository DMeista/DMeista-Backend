package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
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
class UserApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    @Throws
    fun signUpTest() {
        signUp("rkdtlsgml50@naver.com", "1234", "user")
        val user = userRepository.findByNickname("user")?:{ throw Exception() }()
        userRepository.delete(user)
    }


    @Test
    @Throws
    fun nicknameVerifyTest() {
        mvc.perform(get("/users/nickname")
                .param("nickname", "user"))
                .andExpect(status().isOk)
                .andDo(print())
    }


<<<<<<< HEAD
    @Test
    @Throws
    fun changePasswordTest() {
        signUp("rkdtlsgml50@naver.com", "1234", "user")
        emailVerify("rkdtlsgml50@naver.com")
        val request = ChangePasswordRequest("rkdtlsgml50@naver.com", "12345")
        requestMvc(put("/users/password"), request)

        val user = userRepository.findByNickname("user")?:{ throw Exception() }()
        userRepository.delete(user)
    }


    @Test
    fun exitAccountTest() {
        signUp("rkdtlsgml50@naver.com", "1234", "user")
        emailVerify("rkdtlsgml50@naver.com")
        val request = ChangePasswordRequest("rkdtlsgml50@naver.com", "1234")
        val accessToken = accessKey("rkdtlsgml50@naver.com", "1234")

        mvc.perform(delete("/users")
                .header("Authorization", "Bearer $accessToken")
                .content(objectMapper
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andDo(print())
    }

=======
//    @Test
//    @Throws
//    fun T3_changePasswordTest() {
//        val request = ChangePasswordRequest(email, "12345")
//        emailVerifyTest(email)
//        requestMvc(put("/users/password"), request)
//    }
//
//
//    @Test
//    fun T4_exitAccountTest() {
//        password = "12345"
//        emailVerifyTest(email)
//        val accessToken = accessKey()
//        val request = ChangePasswordRequest(email, password)
//
//        mvc.perform(delete("/users")
//                .header("Authorization", "Bearer $accessToken")
//                .content(ObjectMapper()
//                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//                        .writeValueAsString(request))
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk).andDo(print())
//                .andReturn()
//    }
>>>>>>> parent of cf81679... [REFACTOR] ChangePasswordTest


    @Throws
    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any): String {
        return mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andDo(print())
                .andReturn().response.contentAsString
    }


    private fun emailVerifyTest(email: String) {
        emailVerificationRepository.save(EmailVerification(
                email = email,
                authCode = "ASD123",
                status = EmailVerificationStatus.UNVERIFID
        ))

        val request = VerifyCodeRequest(email, "ASD123")
        requestMvc(put("/users/email/verify"), request)
    }


    @Throws
    private fun signUp(email: String, password: String, nickname: String) {
        emailVerifyTest(email)
        val request = SignUpRequest(email, passwordEncoder.encode(password), nickname)
        requestMvc(post("/users"), request)
    }


    @Throws
    private fun signIn(email: String, password: String): String {
        val signInRequest = SignInRequest(email, password)
        return mvc.perform(post("/auth")
                .content(objectMapper
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }

    private fun accessKey(email: String, password: String): String {
        val content = signIn(email, password)
        val response: TokenResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToke
    }
}