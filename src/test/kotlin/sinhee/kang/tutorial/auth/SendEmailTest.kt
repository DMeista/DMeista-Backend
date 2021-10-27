package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import sinhee.kang.tutorial.TestFactory
import sinhee.kang.tutorial.domain.auth.entity.requestLimiter.EmailRequestLimiter

@Suppress("NonAsciiCharacters")
class SendEmailTest: TestFactory() {

    private val testPath = "/auth/verify/email"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        emailRequestLimiterRepository.deleteAll()
        emailVerificationRepository.deleteAll()
    }

    @Test
    fun `회원가입 - 이메일 전송 - Ok`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user2.email) }

        requestParams(post("$testPath/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `회원가입 - 이미 존재하는 유저 - Conflict`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user.email) }

        requestParams(post("$testPath/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `회원가입 - 이메일 최대 요청 에러 - TooManyRequest`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user2.email) }

        emailRequestLimiterRepository.findById(user2.email)
            .orElseGet{ emailRequestLimiterRepository.save(EmailRequestLimiter(user2.email)) }
            .let { limiter ->
                repeat(5) { emailRequestLimiterRepository.save(limiter.update()) }
            }

        requestParams(post("$testPath/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }

    @Test
    fun `회원가입 - 이메일 형식 에러 - BadRequest`() {
        val invalidEmail = "user@email"
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", invalidEmail) }

        requestParams(post("$testPath/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `유저서비스 - 이메일 전송 - Ok`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user.email) }

        requestParams(post("$testPath/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `유저서비스 - 존재하지 않는 유저 - NotFound`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user2.email) }

        requestParams(post("$testPath/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `유저서비스 - 이메일 최대 요청 에러 - isTooManyRequest`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", user.email) }

        emailRequestLimiterRepository.findById(user.email)
            .orElseGet{ emailRequestLimiterRepository.save(EmailRequestLimiter(user.email)) }
            .let { limiter ->
                repeat(5) { emailRequestLimiterRepository.save(limiter.update()) }
            }

        requestParams(post("$testPath/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }

    @Test
    fun `유저서비스 - 이메일 형식 에러 - BadRequest`() {
        val invalidEmail = "user@email"
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("email", invalidEmail) }

        requestParams(post("$testPath/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
