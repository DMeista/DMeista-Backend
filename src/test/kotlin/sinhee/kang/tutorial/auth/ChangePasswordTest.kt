package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.auth.dto.request.ChangePasswordRequest
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidArgumentException

@Suppress("NonAsciiCharacters")
class ChangePasswordTest : TestProperties() {

    private val testPath = "/auth/password"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        emailVerify(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        emailVerificationRepository.deleteAll()
    }

    @Test
    fun `유저 패스워드 변경 - Ok`() {
        val request = ChangePasswordRequest(user.email, password, password)

        requestBody(put(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `이메일 비인증 오류 - Unauthorized`() {
        emailVerificationRepository.deleteAll()
        val request = ChangePasswordRequest(user.email, password, password)

        requestBody(put(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `이메일 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = ChangePasswordRequest("user@email", password, password)

            requestBody(put(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }

    @Test
    fun `패스워드 형식 오류 - BadRequest`() {
        assertThrows<InvalidArgumentException> {
            val request = ChangePasswordRequest(user.email, "password", password)

            requestBody(put(testPath), request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }
}
