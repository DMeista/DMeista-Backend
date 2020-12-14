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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.ChangeEmailRequest
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.request.VerifyCodeRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test", "local")
class UserApiTest(
        @LocalServerPort
        private var port: Int,
        private var context: WebApplicationContext,
        private var mvc: MockMvc,

        private var emailVerificationRepository: EmailVerificationRepository,
        private var userRepository: UserRepository,
        private var passwordEncoder: PasswordEncoder
) {
    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        emailVerificationRepository.save(EmailVerification(
                email = "rkdtlsgml50@naver.com",
                authCode = "ABCDE",
                status = EmailVerificationStatus.UNVERIFID
        ))
    }

    @AfterEach
    fun clean() {
        emailVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Tag("Second")
    @Test
    @Throws(Exception::class)
    fun emailVerifyTest() {
        val request = VerifyCodeRequest("rkdtlsgml50@naver.com", "ABCDE")
        requestMvc(put("/users/email/verify"), request)
    }

    @Tag("Second")
    @Test
    @Throws(Exception::class)
    fun signUpTest() {
        emailVerifyTest()
        val request = SignInRequest("rkdtlsgml50@naver.com", "1234")
        requestMvc(post("/users"), request)
    }

    @Tag("Second")
    @Test
    @Throws(Exception::class)
    fun passwordChangeTest() {
        signUpTest()
        val request = ChangePasswordRequest("rkdtlsgml50@naver.com", "1234")
        requestMvc(put("/users/password"), request)
        val user: User = userRepository.findByEmail("rkdtlsgml50@naver.com")
                ?:{ throw UserNotFoundException() }()
        assert(passwordEncoder.matches("1234", user.password))
    }

    @Tag("Second")
    @Test
    @Throws(Exception::class)
    fun emailChangeTest() {
        signUpTest()
        val request = ChangeEmailRequest("rkdtlsgml50@naver.com", "rkdtlsgml40@naver.com")
        requestMvc(put("/users/new-email"), request)
        val user: User = userRepository.findByEmail("rkdtlsgml40@naver.com")
                ?:{ throw UserNotFoundException() }()
        assert(user.email == "rkdtlsgml40@naver.com")
    }

    @Tag("Second")
    @Test
    @Throws(Exception::class)
    fun exitAccount() {
        signUpTest()
        val request = ChangePasswordRequest("rkdtlsgml40@naver.com", "1234")
        requestMvc(delete(""), request)
    }

    @Throws(Exception::class)
    private fun requestMvc(method: MockHttpServletRequestBuilder, any: Any) {
        val baseUrl = "http://localhost:$port"
        mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(any))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }
}