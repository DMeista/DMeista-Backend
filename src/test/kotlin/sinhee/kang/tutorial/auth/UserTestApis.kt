package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import sinhee.kang.tutorial.TestApis
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
        userRepository.deleteAll()
    }

    @Test
    fun signUpTest() {
        userRepository.deleteAll()
        requestBody(post("/users"), SignUpRequest(user.email, "1234", user.nickname))
        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }

    @Test
    fun changePasswordTest() {
        requestBody(put("/users/password"), ChangePasswordRequest(user.email, "4321"))
    }

    @Test
    fun exitAccountTest() {
        val request = ChangePasswordRequest(user.email, "1234")
        requestBody(delete("/users"), request, token = getAccessToken(signInRequest))
    }

    @Test
    fun userInfoTest() {
        val userInfo = requestBody(get("/users"), token = getAccessToken(signInRequest))
        val response = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(response.username == user.nickname)
    }

    private fun emailVerify() {
        emailVerificationRepository.save(EmailVerification(
                email = user.email,
                authCode = "AUTH_CODE",
                status = EmailVerificationStatus.UNVERIFIED
        ))
        requestBody(put("/users/email/verify"), VerifyCodeRequest(user.email, "AUTH_CODE"))
    }
}
