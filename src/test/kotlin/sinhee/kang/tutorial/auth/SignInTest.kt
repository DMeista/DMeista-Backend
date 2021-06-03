package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest

@Suppress("NonAsciiCharacters")
class SignInTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun `유저 로그인`() {
        requestBody(post("/auth"), signInRequest)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `패스워드 불일치 오류`() {
        val request = SignInRequest(user.email, "${password}a")

        requestBody(post("/auth"), request)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `이메일 형식 오류`() {
        val invalidEmails = arrayListOf("user@email", "user@email")

        for(email: String in invalidEmails) {
            requestBody(post("/auth"), SignInRequest(email, password))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류`() {
        val request = SignInRequest(user.email, "password")

        requestBody(post("/auth"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `존재하지 않는 유저`() {
        userRepository.deleteAll()

        requestBody(post("/auth"), signInRequest)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
