package sinhee.kang.tutorial.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class EmojiApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var emojiRepository: EmojiRepository
    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    @Throws
    fun addEmojiTest() {
        val post = uploadPost()
        val emoji = emojiPost(post, EmojiStatus.LIKE)
        val response = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(emoji, EmojiResponse::class.java)
        assert(post == response.postId)
        emojiRepository.deleteAll()
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun changeEmojiTest() {
        val post = uploadPost()
        emojiPost(post, EmojiStatus.LIKE)
        emojiPost(post, EmojiStatus.NICE)

        emojiRepository.deleteAll()
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun removeEmojiTest() {
        val post = uploadPost()
        emojiPost(post, EmojiStatus.LIKE)
        emojiPost(post, EmojiStatus.LIKE)

        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun getPostEmojiListTest() {
        
    }


    @Throws
    fun emojiPost(postId: Int, status: EmojiStatus): String {
        val accessToken = accessKey()
        return mvc.perform(post("/posts/$postId/emoji")
                .header("Authorization", "Bearer $accessToken")
                .param("status", "$status"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }

    @Throws
    fun uploadPost(): Int {
        val accessToken = accessKey()
        return Integer.parseInt(mvc.perform(post("/posts")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tags"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString)
    }

    @Throws
    private fun signIn(): MvcResult {
        val signInRequest = SignInRequest("rkdtlsgml50@naver.com", "1234")
        return mvc.perform(post("/auth")
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn()
    }


    private fun accessKey(): String {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToke
    }
}