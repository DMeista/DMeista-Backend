package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidArgumentException

@Suppress("NonAsciiCharacters")
class SignInTest: TestProperties() {

    private val testPath = "/auth/login"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun `유저 로그인 - Ok`() {
        requestBody(post(testPath), SignInRequest(user.email, password))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `패스워드 불일치 오류 - Forbidden`() {
        val request = SignInRequest(user.email, "${password}a")

        requestBody(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `존재하지 않는 유저- NotFound`() {
        requestBody(post(testPath), signInRequest2)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `이메일 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = SignInRequest("user@email", password)

            requestBody(post(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = SignInRequest(user.email, "password")

            requestBody(post(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }
}
