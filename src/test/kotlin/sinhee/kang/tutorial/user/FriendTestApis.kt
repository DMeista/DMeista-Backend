package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TestApis
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

class FriendTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        userRepository
            .apply {
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
    fun getReceiveFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = currentUserToken)
        val request = mappingResponse(
            requestBody(get("/users/friends/request"), token = targetUserToken),
            UserListResponse::class.java
        ) as UserListResponse

        assert(value = request.applicationResponses[0].nickname == user.nickname)
    }

    @Test
    fun sendFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = currentUserToken)
        assert(isCheckUserAndTargetUserExist(user, user2))
    }

    @Test
    fun acceptFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = currentUserToken)
        requestParam(put("/users/friends"), targetName = user.nickname, token = targetUserToken)

        assert(friendRepository.findByUserIdAndTargetIdAndStatus(user, user2, FriendStatus.ACCEPT)
                ?.let { true }
                ?: false
        )
    }

    @Test
    fun deniedFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = currentUserToken)
        requestParam(delete("/users/friends"), targetName = user.nickname, token = targetUserToken)

        assert(!isCheckUserAndTargetUserExist(user, user2))
    }

    @Test
    fun deleteFriendTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = currentUserToken)
        requestParam(put("/users/friends"), targetName = user.nickname, token = targetUserToken)
        requestParam(delete("/users/friends"), targetName = user.nickname, token = targetUserToken)

        assert(!isCheckUserAndTargetUserExist(user, user2))
    }

    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?: false
    }

    private fun requestParam(method: MockHttpServletRequestBuilder, targetName: String? = null, token: String?): String {
        return mvc.perform(
            method
                .header("Authorization", token)
                .param("nickname", targetName))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
