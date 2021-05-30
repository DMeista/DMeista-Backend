package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.TestApis
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

class UserTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        emailVerify()
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        emailVerificationRepository.deleteAll()
        emailLimiterRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun signUpTest() {
        userRepository.deleteAll()
        requestBody(post("/users"), SignUpRequest(user.email, password, user.nickname))
        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }

    @Test
    fun changePasswordTest() {
        requestBody(put("/users/password"), ChangePasswordRequest(user.email, password))
    }

    @Test
    fun exitAccountTest() {
        val request = ChangePasswordRequest(user.email, password)
        requestBody(delete("/users"), request, token = getAccessToken(signInRequest))
    }

    @Test
    fun userInfoTest() {
        val userInfo = requestBody(get("/users"), token = getAccessToken(signInRequest))
        val response = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(response.username == user.nickname)
    }

    @Test
    fun sendEmailTest() {
        requestBody(post("/users/email/verify/USER"), EmailRequest(user.email))
        requestBody(post("/users/email/verify/REGISTER"), EmailRequest(user2.email))
    }

    @Test
    fun tooManyRequestErrorTest() {
        userRepository.deleteAll()
        emailLimiterRepository.findById(user.email)
            .orElseGet{ emailLimiterRepository.save(EmailLimiter(user.email)) }
            .let { limit ->
                (0..9).map { emailLimiterRepository.save(limit.update()) }
            }

        mvc.perform(
            post("/users/email/verify/REGISTER")
                .content(ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .writeValueAsString(EmailRequest(user.email)))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }

    private fun emailVerify() {
        emailVerificationRepository.save(EmailVerification(
                email = user.email,
                authCode = "AUTH_CODE",
                status = EmailVerificationStatus.VERIFIED
        ))
        requestBody(put("/users/email/verify"), VerifyCodeRequest(user.email, "AUTH_CODE"))
    }
}
