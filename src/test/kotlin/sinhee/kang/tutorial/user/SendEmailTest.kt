package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter
import sinhee.kang.tutorial.domain.auth.dto.request.EmailRequest

@Suppress("NonAsciiCharacters")
class SendEmailTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        emailLimiterRepository.deleteAll()
        signUpVerificationRepository.deleteAll()
    }

    @Test
    fun `회원가입 - 이메일 전송`() {
        val request = EmailRequest(user2.email)

        requestBody(post("/users/email/verify/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `회원가입 - 이미 존재하는 유저`() {
        val request = EmailRequest(user.email)

        requestBody(post("/users/email/verify/REGISTER"), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `유저서비스 - 이메일 전송`() {
        val request = EmailRequest(user.email)

        requestBody(post("/users/email/verify/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `유저서비스 - 유저가 존재하지 않음`() {
        val request = EmailRequest(user2.email)

        requestBody(post("/users/email/verify/USER"), request)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `이메일 유효성 확인`() {
        val invalidEmails = arrayListOf("user@email", "user@email")

        for(email: String in invalidEmails) {
            requestBody(post("/users/email/verify/USER"), EmailRequest(email))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `유저서비스 - 이메일 최대 요청 에러`() {
        emailLimiterRepository.findById(user.email)
            .orElseGet{ emailLimiterRepository.save(EmailLimiter(user.email)) }
            .let { limit ->
                (0..9).map { emailLimiterRepository.save(limit.update()) }
            }

        requestBody(post("/users/email/verify/USER"), EmailRequest(user.email))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }

    @Test
    fun `회원가입 - 이메일 최대 요청 에러`() {
        emailLimiterRepository.findById(user2.email)
            .orElseGet{ emailLimiterRepository.save(EmailLimiter(user2.email)) }
            .let { limit ->
                (0..9).map { emailLimiterRepository.save(limit.update()) }
            }

        requestBody(post("/users/email/verify/REGISTER"), EmailRequest(user2.email))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }
}