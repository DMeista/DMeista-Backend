package sinhee.kang.tutorial.auth.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest

@Suppress("NonAsciiCharacters")
class ChangePasswordTest: TestLib() {

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
    fun `패스워드 변경`() {
        val request = ChangePasswordRequest(user.email, password)

        requestBody(put("/users/password"), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `이메일 형식 오류`() {
        val invalidEmails = arrayListOf("user@email", "user@email")

        for(email: String in invalidEmails) {
            requestBody(put("/users/password"), ChangePasswordRequest(email, password))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류`() {
        val request = ChangePasswordRequest(user.email, "password")

        requestBody(put("/users/password"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `이메일 비인증 오류`() {
        signUpVerificationRepository.deleteAll()

        val request = ChangePasswordRequest(user.email, password)

        requestBody(put("/users/password"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}