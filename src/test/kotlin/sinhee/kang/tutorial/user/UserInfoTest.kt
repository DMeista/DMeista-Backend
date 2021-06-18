package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

@Suppress("NonAsciiCharacters")
class UserInfoTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        userRepository.save(user2)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun `해당 유저 정보 로드`() {
        val token = getAccessToken(signInRequest)

        val userInfo = requestBody(get("/users"), token = token)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
        val userInfoResponse = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(userInfoResponse.email == user.email)
    }

    @Test
    fun `타 유저 정보 로드`() {
        val token = getAccessToken(signInRequest)
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        val userInfo = requestParams(get("/users"), params, token)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
        val userInfoResponse = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse

        assert(userInfoResponse.username == user2.nickname)
    }

    @Test
    fun `유저가 존재하지 않음`() {
        val token = getAccessToken(signInRequest)
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", "user2.nickname") }

        requestParams(get("/users"), params, token)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}