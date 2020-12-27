package sinhee.kang.tutorial.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FriendApiTest {
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

    val testMail = "rkdtlsgml50@naver.com"
    val username = "user1"

    val testMail2 = "rkdtlsgml40@naver.com"
    val username2 = "user2"

    val passwd = "1234"

    @Before
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
    }

    @After
    fun clean() {
        friendRepository.deleteAll()
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }
        userRepository.findByNickname(username2)
                ?.let { user -> userRepository.delete(user) }
    }


    @Test
    @Throws
    fun getAcceptFriendListTest() {
        val user = userRepository.findByNickname(username)
                ?:{ throw UserNotFoundException() }()
        val targetUser = userRepository.findByNickname(username2)
                ?:{ throw UserNotFoundException() }()

        val user1Token = "Bearer ${accessToken(testMail, passwd)}"
        val user2Token = "Bearer ${accessToken(testMail2, passwd)}"

        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        requestMvc(put("/users/friends/${user.id}"), token = user2Token)
        val request = mappingResponse(requestMvc(get("/users/user1/friends"), token = user1Token), UserListResponse::class.java) as UserListResponse
        assert(request.applicationResponses[0].nickname == targetUser.nickname)
    }


    @Test
    @Throws
    fun getReceiveFriendRequestTest() {
        val user = userRepository.findByNickname(username)
                ?:{ throw UserNotFoundException() }()
        val targetUser = userRepository.findByNickname(username2)
                ?:{ throw UserNotFoundException() }()

        val user1Token = "Bearer ${accessToken(testMail, passwd)}"
        val user2Token = "Bearer ${accessToken(testMail2, passwd)}"

        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        val request = mappingResponse(requestMvc(get("/users/friends"), token = user2Token), UserListResponse::class.java) as UserListResponse
        assert(request.applicationResponses[0].nickname == user.nickname)
    }


    @Test
    @Throws
    fun sendFriendRequestTest() {
        val user = userRepository.findByNickname(username)
                ?:{ throw UserNotFoundException() }()
        val targetUser = userRepository.findByNickname(username2)
                ?:{ throw UserNotFoundException() }()

        val user1Token = "Bearer ${accessToken(testMail, passwd)}"

        requestMvc(post("/users/friends/${targetUser.id}"), token = user1Token)
        assert(isCheckUserAndTargetUserExist(user, targetUser))
    }


    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?:{ false }()
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