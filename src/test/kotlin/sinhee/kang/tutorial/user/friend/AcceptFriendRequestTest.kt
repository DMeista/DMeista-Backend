package sinhee.kang.tutorial.user.friend

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.entity.friend.Friend

@Suppress("NonAsciiCharacters")
class AcceptFriendRequestTest : TestProperties() {

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
    fun `친구 요청 수락 - Ok`() {
        friendRepository.save(
            Friend(
                user = user,
                targetUser = user2
            )
        )
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(put(testPath), request, targetUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assert(isConnect(user, user2, FriendStatus.ACCEPT))
    }

    @Test
    fun `친구 요청이 존재하지 않는 경우 - NotFound`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(put(testPath), request, targetUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `친구 요청 중복 수락 - NotFound`() {
        friendRepository.save(
            Friend(
                user = user,
                targetUser = user2
            )
        )
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(put(testPath), request, targetUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        requestParams(put(testPath), request, targetUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `타겟유저가 아닌 유저가 수락 요청을 할 경우 - NotFound`() {
        friendRepository.save(
            Friend(
                user = user,
                targetUser = user2
            )
        )
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(put(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `타겟 유저가 존재하지 않음 - NotFound`() {
        friendRepository.save(
            Friend(
                user = user,
                targetUser = user2
            )
        )
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", "nickname") }

        requestParams(put(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        friendRepository.save(
            Friend(
                user = user,
                targetUser = user2
            )
        )
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", "nickname") }

        requestParams(put(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
