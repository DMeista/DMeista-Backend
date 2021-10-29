package sinhee.kang.tutorial.user.userInfo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

@Suppress("NonAsciiCharacters")
class UserInfoTest : TestProperties() {

    private val testPath = "/users"

    @BeforeEach
    fun setup() {
        userRepository.apply {
            save(user)
            save(user2)
        }
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun `해당 유저 정보 로드 - Ok`() {
        val token = getAccessToken(signInRequest)

        val userInfo = requestBody(get(testPath), token = token)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
        val userInfoResponse = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(userInfoResponse.email == user.email)
    }

    @Test
    fun `타 유저 정보 로드 - Ok`() {
        val token = getAccessToken(signInRequest)
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        val userInfo = requestParams(get(testPath), params, token)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
        val userInfoResponse = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(userInfoResponse.username == user2.nickname)
    }

    @Test
    fun `유저가 존재하지 않음 - NotFound`() {
        val token = getAccessToken(signInRequest)
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", "user2.nickname") }

        requestParams(get(testPath), params, token)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
