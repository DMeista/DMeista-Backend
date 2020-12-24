package sinhee.kang.tutorial.post

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommentApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    val testMail = "rkdtlsgml50@naver.com"
    val passwd = "1234"
    val username = "user"

    @Before
    fun setup() {
        userRepository.save(User(
                email = testMail,
                password = passwordEncoder.encode(passwd),
                nickname = username
        ))
    }

    @After
    fun clean() {
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }

    }

    // TODO: Upload Comment, subComment
    @Test
    @Throws
    fun uploadCommentTest() {
    }


    @Test
    @Throws
    fun uploadSubCommentTest() {
    }


    // TODO: Change Comment, subComment
    @Test
    @Throws
    fun changeCommentTest() {
    }


    @Test
    @Throws
    fun changeSubCommentTest() {
    }


    // TODO: Delete Comment, subComment
    @Test
    @Throws
    fun deleteCommentTest() {
    }


    @Test
    @Throws
    fun deleteSubCommentTest() {
    }


    private fun uploadPost(): Int {
        val accessToken = accessToken()
        return Integer.parseInt(mvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tags"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn().response.contentAsString)
    }


    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any? = null): String {
        return mvc.perform(
                method
                        .content(ObjectMapper()
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                .writeValueAsString(obj))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn().response.contentAsString
    }


    private fun accessToken(): String {
        val content = requestMvc(MockMvcRequestBuilders.post("/auth"), SignInRequest(testMail, passwd))
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse
        return response.accessToken
    }


    private fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(obj, cls)
    }
}