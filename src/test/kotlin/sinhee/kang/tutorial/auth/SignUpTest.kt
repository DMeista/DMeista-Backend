package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.auth.dto.request.SignUpRequest
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidArgumentException

@Suppress("NonAsciiCharacters")
class SignUpTest : TestProperties() {

    private val testPath = "/auth/register"

    @BeforeEach
    fun setup() {
        emailVerify(user)
    }

    @AfterEach
    fun clean() {
        emailVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `유저 회원가입 - Ok`() {
        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post(testPath), request)

        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }

    @Test
    fun `이메일 비인증 오류 - Unauthorized`() {
        emailVerificationRepository.deleteAll()

        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `이미 가입된 이메일 - Conflict`() {
        userRepository.save(user)
        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `이미 가입된 닉네임 - Conflict`() {
        userRepository.save(user)
        val request = SignUpRequest(user.email, password, user.nickname)

        requestBody(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `이메일 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = SignUpRequest("user@email", password, user.nickname)

            requestBody(post(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = SignUpRequest(user.email, "password", user.nickname)

            requestBody(post(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }
}
