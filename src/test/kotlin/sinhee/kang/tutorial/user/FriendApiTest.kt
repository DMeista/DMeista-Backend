package sinhee.kang.tutorial.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException

class FriendApiTest: ApiTest() {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var friendRepository: FriendRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val testMail = "rkdtlsgml500@naver.com"
    private val username = "user1"

    private val testMail2 = "rkdtlsgml400@naver.com"
    private val username2 = "user2"

    private val passwd = "1234"

    private lateinit var user1Token: String
    private lateinit var user2Token: String

    private lateinit var user: User
    private lateinit var targetUser: User


    @BeforeEach
    fun setup() {
        userRepository.save(User(
                email = testMail,
                password = passwordEncoder.encode(passwd),
                nickname = username
        ))
        userRepository.save(User(
                email = testMail2,
                password = passwordEncoder.encode(passwd),
                nickname = username2
        ))
        user1Token = "Bearer ${accessToken(testMail, passwd)}"
        user2Token = "Bearer ${accessToken(testMail2, passwd)}"

        user = userRepository.findByNickname(username)
                ?: throw UserNotFoundException()
        targetUser = userRepository.findByNickname(username2)
                ?: throw UserNotFoundException()
    }

    @AfterEach
    fun clean() {
        friendRepository.deleteAll()
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }
        userRepository.findByNickname(username2)
                ?.let { user -> userRepository.delete(user) }
    }


    @Test
    @Throws
    fun getReceiveFriendRequestTest() {
        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        val request = mappingResponse(requestMvc(get("/users/friends"), token = user2Token), UserListResponse::class.java) as UserListResponse

        assert(request.applicationResponses[0].nickname == user.nickname)
    }


    @Test
    @Throws
    fun sendFriendRequestTest() {
        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        assert(isCheckUserAndTargetUserExist(user, targetUser))
    }


    @Test
    @Throws
    fun acceptFriendRequestTest() {
        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(put("/users/friends/${user.id}"), token = user2Token)

        assert(friendRepository.findByUserIdAndTargetIdAndStatus(user, targetUser, FriendStatus.ACCEPT)
                ?.let { true }
                ?: false
        )
    }


    @Test
    @Throws
    fun deniedFriendRequestTest() {
        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(delete("/users/friends/${user.id}"), token = user2Token)

        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(delete("/users/friends/${targetUser.id}"), token = user1Token)

        assert(friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { throw Exception() }
                ?: true
        )
    }


    @Test
    @Throws
    fun deleteFriendTest() {
        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(put("/users/friends/${user.id}"), token = user2Token)
        requestMvc(delete("/users/friends/${user.id}"), token = user2Token)

        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(put("/users/friends/${user.id}"), token = user2Token)
        requestMvc(delete("/users/friends/${targetUser.id}"), token = user1Token)

        assert(friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { throw Exception() }
                ?: true
        )
    }


    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?: false
    }


    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any? = null, token: String? = ""): String {
        return mvc.perform(
                method
                        .header("Authorization", token)
                        .content(objectMapper
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                .writeValueAsString(obj))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    private fun accessToken(email: String, passwd: String): String {
        val content = requestMvc(post("/auth"), SignInRequest(email, passwd))
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse
        return response.accessToken
    }


    private fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(obj, cls)
    }
}