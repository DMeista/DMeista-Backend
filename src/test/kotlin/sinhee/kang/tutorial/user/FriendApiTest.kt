package sinhee.kang.tutorial.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import javax.servlet.http.Cookie

class FriendApiTest: ApiTest() {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var friendRepository: FriendRepository

    private var user1Cookie: Cookie? = null
    private var user2Cookie: Cookie? = null

    private val user1: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user1",
        password = passwordEncoder.encode("1234")
    )

    private val user2: User = User(
        email = "rkdtlsgml50@dsm.hs.kr",
        nickname = "user2",
        password =  passwordEncoder.encode("1234")
    )

    @BeforeEach
    fun setup() {
        userRepository.apply {
            save(user1)
            save(user2)
        }
        user1Cookie = login(SignInRequest(user1.email, "1234"))
        user2Cookie = login(SignInRequest(user2.email, "1234"))
    }


    @AfterEach
    fun clean() {
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }


    @Test
    @Throws
    fun getReceiveFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, cookie = user1Cookie)
        val request = mappingResponse(
            requestBody(get("/users/friends/request"), cookie = user2Cookie),
            UserListResponse::class.java
        ) as UserListResponse

        assert(value = request.applicationResponses[0].nickname == user1.nickname)
    }


    @Test
    @Throws
    fun sendFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, cookie = user1Cookie)
        assert(isCheckUserAndTargetUserExist(user1, user2))
    }


    @Test
    @Throws
    fun acceptFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, cookie = user1Cookie)
        requestParam(put("/users/friends"), targetName = user1.nickname, cookie = user2Cookie)

        assert(friendRepository.findByUserIdAndTargetIdAndStatus(user1, user2, FriendStatus.ACCEPT)
                ?.let { true }
                ?: false
        )
    }


    @Test
    @Throws
    fun deniedFriendRequestTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, cookie = user1Cookie)
        requestParam(delete("/users/friends"), targetName = user1.nickname, cookie = user2Cookie)

        assert(!isCheckUserAndTargetUserExist(user1, user2))
    }


    @Test
    @Throws
    fun deleteFriendTest() {
        requestParam(post("/users/friends"), targetName = user2.nickname, cookie = user1Cookie)
        requestParam(put("/users/friends"), targetName = user1.nickname, cookie = user2Cookie)
        requestParam(delete("/users/friends"), targetName = user1.nickname, cookie = user2Cookie)

        assert(!isCheckUserAndTargetUserExist(user1, user2))
    }


    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?: false
    }


    private fun requestParam(method: MockHttpServletRequestBuilder, targetName: String? = null, cookie: Cookie?): String {
        return mvc.perform(
            method
                .param("nickname", targetName)
                .cookie(cookie))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
