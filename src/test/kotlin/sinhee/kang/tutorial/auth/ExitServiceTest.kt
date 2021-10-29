package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidArgumentException

@Suppress("NonAsciiCharacters")
class ExitServiceTest : TestProperties() {

    private val testPath = "/auth"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        emailVerify(user)
    }

    @AfterEach
    fun clean() {
        emailVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `유저 회원탈퇴 - Ok`() {
        val token = getAccessToken(signInRequest)
        val request = SignInRequest(user.email, password)

        requestBody(delete(testPath), request, token)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `패스워드 불일치 오류 - Forbidden`() {
        val token = getAccessToken(signInRequest)
        val request = SignInRequest(user.email, password + "123")

        requestBody(delete(testPath), request, token)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request = SignInRequest(user.email, password)

        requestBody(delete(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `이메일 비인증 오류 - Unauthorized`() {
        emailVerificationRepository.deleteAll()

        val token = getAccessToken(signInRequest)
        val request = SignInRequest(user.email, password)

        requestBody(delete(testPath), request, token)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `이메일 형식 오류 - BadRequest`() {
        val token = getAccessToken(signInRequest)
        assertThrows<InvalidArgumentException> {
            val request = SignInRequest("user@email", password)

            requestBody(delete(testPath), request, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류 - BadRequest`() {
        val token = getAccessToken(signInRequest)
        assertThrows<InvalidArgumentException> {
            val request = SignInRequest(user.email, "password")

            requestBody(delete(testPath), request, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }
}
