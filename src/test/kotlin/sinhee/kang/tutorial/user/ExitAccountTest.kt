package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest

@Suppress("NonAsciiCharacters")
class ExitAccountTest: TestLib() {

    @BeforeEach
    fun setup() {
        emailVerify(user)
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        signUpVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `유저 회원탈퇴`() {
        val token = getAccessToken(signInRequest)
        val request = ChangePasswordRequest(user.email, password)

        requestBody(delete("/users"), request, token)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `패스워드 불일치 오류`() {
        val token = getAccessToken(signInRequest)
        val request = ChangePasswordRequest(user.email, "${password}a")

        requestBody(delete("/users"), request, token)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `이메일 형식 오류`() {
        val token = getAccessToken(signInRequest)
        val invalidEmails = arrayListOf("user@email", "user@email")

        for(email: String in invalidEmails) {
            requestBody(delete("/users"), ChangePasswordRequest(email, password), token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류`() {
        val token = getAccessToken(signInRequest)
        val request = ChangePasswordRequest(user.email, "password")

        requestBody(delete("/users"), request, token)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `이메일 비인증 오류`() {
        signUpVerificationRepository.deleteAll()

        val token = getAccessToken(signInRequest)
        val request = ChangePasswordRequest(user.email, password)

        requestBody(delete("/users"), request, token)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}