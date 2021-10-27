package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestFactory

@Suppress("NonAsciiCharacters")
class ExtendTokenTest: TestFactory() {

    private val testPath = "/auth"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun `토큰 연장 - Ok`() {
        val refreshCookie = getRefreshToken(signInRequest)

        requestBody(put(testPath), cookie = refreshCookie)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `유효하지 않은 토큰 - Unauthorized`() {
        val refreshCookie = getRefreshToken(signInRequest)
            ?.apply { value = "$value " }

        requestBody(put(testPath), cookie = refreshCookie)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
