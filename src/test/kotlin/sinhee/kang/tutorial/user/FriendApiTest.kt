package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TokenType
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

class FriendApiTest: ApiTest() {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var friendRepository: FriendRepository

    private val user1: User = User(
        nickname = "user1",
        email = "rkdtlsgml500@naver.com",
        password = passwordEncoder.encode("1234")
    )

    private val user2: User = User(
        nickname = "user2",
        email = "rkdtlsgml400@naver.com",
        password =  passwordEncoder.encode("1234")
    )

    private lateinit var user1Token: String
    private lateinit var user2Token: String

    @BeforeEach
    fun setup() {
        userRepository.save(user1)
        userRepository.save(user2)
        user1Token = "Bearer ${getToken(TokenType.ACCESS, user1.email, "1234")}"
        user2Token = "Bearer ${getToken(TokenType.ACCESS, user2.email, "1234")}"
    }


    @AfterEach
    fun clean() {
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }


    @Test
    @Throws
    fun getReceiveFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = user1Token)
        val request = mappingResponse(
            requestBody(get("/users/friends/request"), token = user2Token),
            UserListResponse::class.java
        ) as UserListResponse

        assert(value = request.applicationResponses[0].nickname == user1.nickname)
    }


    @Test
    @Throws
    fun sendFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = user1Token)
        assert(isCheckUserAndTargetUserExist(user1, user2))
    }


    @Test
    @Throws
    fun acceptFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = user1Token)
        requestParam(put("/users/friends"), targetName = user1.nickname, token = user2Token)

        assert(friendRepository.findByUserIdAndTargetIdAndStatus(user1, user2, FriendStatus.ACCEPT)
                ?.let { true }
                ?: false
        )
    }


    @Test
    @Throws
    fun deniedFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = user1Token)
        requestParam(delete("/users/friends"), targetName = user1.nickname, token = user2Token)

        assert(!isCheckUserAndTargetUserExist(user1, user2))
    }


    @Test
    @Throws
    fun deleteFriendTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, token = user1Token)
        requestParam(put("/users/friends"), targetName = user1.nickname, token = user2Token)
        requestParam(delete("/users/friends"), targetName = user1.nickname, token = user2Token)

        assert(!isCheckUserAndTargetUserExist(user1, user2))
    }


    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?: false
    }


    private fun requestParam(method: MockHttpServletRequestBuilder, targetName: String? = null, token: String? = ""): String {
        return mvc.perform(
            method
                .header("Authorization", token)
                .param("nickname", targetName))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
