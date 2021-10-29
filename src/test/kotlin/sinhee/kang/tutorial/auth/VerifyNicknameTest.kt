package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import sinhee.kang.tutorial.TestProperties

@Suppress("NonAsciiCharacters")
class VerifyNicknameTest : TestProperties() {

    private val testPath = "/auth/verify/nickname"

    @Test
    fun `닉네임 중복검사 오류 - Ok`() {
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(MockMvcRequestBuilders.get(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `닉네임 중복검사 오류 - Conflict`() {
        userRepository.save(user)
        val request = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(MockMvcRequestBuilders.get(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }
}
