package sinhee.kang.tutorial.user.friend

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestFactory

import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

@Suppress("NonAsciiCharacters")
class GetFriendRequestTest: TestFactory() {

    private val testPath = "/users/friends/request"

    @BeforeEach
    fun setup() {
        userRepository.apply {
            save(user)
            save(user2)
        }
        currentUserToken = getAccessToken(signInRequest)
        targetUserToken = getAccessToken(signInRequest2)
    }

    @AfterEach
    fun clean() {
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `받은 친구요청 - Ok`() {
        friendRepository.save(Friend(user2, user))

        val response = requestBody(get(testPath), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val userListResponse = mappingResponse(response, UserListResponse::class.java) as UserListResponse

        assert(userListResponse.applications[0].nickname == user2.nickname)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        requestBody(get(testPath))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
