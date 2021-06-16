package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest
import sinhee.kang.tutorial.domain.auth.dto.request.SignUpRequest

@Suppress("NonAsciiCharacters")
class SignUpTest: TestLib() {

    @BeforeEach
    fun setup() {
        emailVerify(user)
    }

    @AfterEach
    fun clean() {
        signUpVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `유저 회원가입`() {
        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post("/users"), request)

        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }

    @Test
    fun `이메일 형식 오류`() {
        val invalidEmails = arrayListOf("user@email", "user@email")

        for(email: String in invalidEmails) {
            requestBody(post("/users"), SignUpRequest(email, password, user.nickname))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류`() {
        val request = SignUpRequest(user.email, "password", user.nickname)

        requestBody(post("/users"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `이메일 비인증 오류`() {
        signUpVerificationRepository.deleteAll()

        val request = SignUpRequest(user.email, password, "")

        requestBody(post("/users"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `닉네임 중복검사 불일치`() {
        val request = SignUpRequest(user.email, password, " ")

        requestBody(post("/users"), request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `이미 가입된 이메일`() {
        userRepository.save(user)

        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post("/users"), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `이미 가입된 닉네임`() {
        userRepository.save(user)
        emailVerify(user2)

        val request = SignUpRequest(user2.email, password, user.nickname)

        requestBody(post("/users"), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }
}