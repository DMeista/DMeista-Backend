package sinhee.kang.tutorial.friend

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

@Suppress("NonAsciiCharacters")
class ReceiveFriendRequestTest: TestLib() {

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
    fun `받은 친구요청`() {
        friendRepository.save(Friend(
            user = user2,
            targetUser = user
        ))
        val response = requestBody(get("/users/friends/request"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val userListResponse = mappingResponse(response, UserListResponse::class.java) as UserListResponse

        assert(userListResponse.applicationResponses[0].nickname == user2.nickname)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        requestBody(get("/users/friends/request"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
