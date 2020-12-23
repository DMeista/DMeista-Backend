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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PostApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper



    @Test
    @Throws
    fun getAllHashTagPostList_LoadTest() {
        val post: String = requestMvc(get("/posts"))
        val response: PostListResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostListResponse::class.java)
        assert(response.totalItems == postRepository.findAll().count())
    }


    @Test
    @Throws
    fun getHashTagGroupPostList_LoadTest() {
        val post: String = mvc.perform(get("/posts")
                .param("tags", "안경"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val response = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostListResponse::class.java)
        assert(response.totalItems == 2)
    }


    @Test
    @Throws
    fun getPostContentTest() {
        val post: String = requestMvc(get("/posts/78"))
        val response = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostContentResponse::class.java)
        assert(response.title == "타이틀")
    }

    @Test
    @Throws
    fun uploadPostTest() {
        val upload = uploadPost()
        val post = postRepository.findById(upload).orElseThrow { Exception() }
        assert(upload == post.postId)
        postRepository.deleteById(upload)
    }


    @Test
    @Throws
    fun deletePostTest() {
        val post = uploadPost()
        val accessToken = accessKey()
        mvc.perform(delete("/posts/$post")
                .header("Authorization", "Bearer $accessToken"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    @Throws
    private fun requestMvc(method: MockHttpServletRequestBuilder): String {
        return mvc.perform(method)
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
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


    @Throws
    fun uploadPost(): Int {
        val accessToken = accessKey()
        return Integer.parseInt(mvc.perform(post("/posts")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "junit4 test")
                .param("content", "content")
                .param("tags", "junit4"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString)
    }

    private fun accessKey(): String {
        val content: String = signIn().response.contentAsString
        val response: TokenResponse = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToke
    }
}