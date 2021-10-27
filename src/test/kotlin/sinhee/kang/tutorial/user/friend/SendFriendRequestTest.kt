package sinhee.kang.tutorial.user.friend

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestFactory

import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus

@Suppress("NonAsciiCharacters")
class SendFriendRequestTest: TestFactory() {

    private val testPath = "/users/friends"

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
    fun `친구 요청 - Ok`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assert(isConnect(user, user2, FriendStatus.REQUEST))
    }

    @Test
    fun `타겟 유저가 존재하지 않음 - NotFound`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", "nickname") }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `타겟 유저와 현재 유저와 같은 경우 - BadRequest`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `이미 친구 요청이 간 경우 - BadRequest`() {
        friendRepository.save(Friend(
            user = user,
            targetUser = user2
        ))
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
   }

    @Test
    fun `타겟 유저가 친구 요청을 보냈을 경우 - BadRequest`() {
        friendRepository.save(Friend(
            user = user2,
            targetUser = user
        ))
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
