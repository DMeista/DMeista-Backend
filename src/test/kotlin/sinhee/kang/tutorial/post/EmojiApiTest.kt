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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmojiApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var emojiRepository: EmojiRepository
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val testMail = "rkdtlsgml50@naver.com"
    private val passwd = "1234"
    private val username = "user"


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


    @Test
    @Throws
    fun addEmojiTest() {
        val post = uploadPost()
        requestEmoji(post, EmojiStatus.LIKE)
                .let { emoji ->
                    val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
                    assert(post == response.postId)
                }

        emojiRepository.deleteAll()
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun changeEmojiTest() {
        val post = uploadPost()
        requestEmoji(post, EmojiStatus.LIKE)
        requestEmoji(post, EmojiStatus.NICE)

        emojiRepository.deleteAll()
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun removeEmojiTest() {
        val post = uploadPost()
        requestEmoji(post, EmojiStatus.LIKE)
        requestEmoji(post, EmojiStatus.LIKE)

        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun getPostEmojiListTest() {
        val post = uploadPost()
        requestEmoji(post, EmojiStatus.LIKE)
        val emojiList = requestEmojiList(post)
        assert(emojiList.applicationResponses[0].emojiStatus == EmojiStatus.LIKE)

        emojiRepository.deleteAll()
        postRepository.deleteById(post)
    }


    private fun uploadPost(): Int {
        val accessToken = accessToken()
        return Integer.parseInt(mvc.perform(post("/posts")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tags"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString)
    }


    private fun requestEmoji(postId: Int, status: EmojiStatus): String {
        val accessToken = accessToken()
        return mvc.perform(post("/posts/$postId/emoji")
                .header("Authorization", "Bearer $accessToken")
                .param("status", "$status"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    private fun requestEmojiList(postId: Int): PostEmojiListResponse {
        return requestMvc(get("/posts/$postId/emoji"))
                .let { mappingResponse(it, PostEmojiListResponse::class.java) as PostEmojiListResponse }
    }


    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any? = null): String {
        return mvc.perform(
                method
                        .content(ObjectMapper()
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                .writeValueAsString(obj))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    private fun accessToken(): String {
        val content = requestMvc(post("/auth"), SignInRequest(testMail, passwd))
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse
        return response.accessToken
    }


    private fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(obj, cls)
    }
}