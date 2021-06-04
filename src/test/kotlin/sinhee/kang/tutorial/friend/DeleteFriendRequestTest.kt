package sinhee.kang.tutorial.friend

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus

@Suppress("NonAsciiCharacters")
class DeleteFriendRequestTest: TestLib() {

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
    fun `타겟 유저 친구 요청 삭제`() {
        friendRepository.save(Friend(
            user = user,
            targetUser = user2,
            status = FriendStatus.REQUEST
        ))
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user.nickname) }

        requestParams(delete("/users/friends"), request, token = targetUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assert(!isCheckUserAndTargetUserExist(user, user2))
    }

    @Test
    fun `보낸 친구 요청 삭제`() {
        friendRepository.save(Friend(
            user = user,
            targetUser = user2,
            status = FriendStatus.REQUEST
        ))
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(delete("/users/friends"), request, token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assert(!isCheckUserAndTargetUserExist(user, user2))
    }

    @Test
    fun `수락된 친구요청 삭제`() {
        friendRepository.save(Friend(
            user = user,
            targetUser = user2,
            status = FriendStatus.ACCEPT
        ))
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(delete("/users/friends"), request, token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assert(!isCheckUserAndTargetUserExist(user, user2))
    }

    @Test
    fun `친구요청이 없을 경우`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("nickname", user2.nickname) }

        requestParams(delete("/users/friends"), request, token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
