package sinhee.kang.tutorial.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

class PostApiTest: ApiTest() {
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

    private val testMail = "rkdtlsgml50@naver.com"
    private val passwd = "1234"
    private val username = "user"


    @BeforeEach
    fun setup() {
        userRepository.save(User(
                email = testMail,
                password = passwordEncoder.encode(passwd),
                nickname = username
        ))
    }

    @AfterEach
    fun clean() {
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }
    }


    @Test
    @Throws
    fun getAllHashTagPostList_LoadTest() {
        val post: String = requestMvc(get("/posts"))
        val response = mappingResponse(post, PostListResponse::class.java) as PostListResponse
        assert(response.totalItems == postRepository.findAll().count())
    }


    @Test
    @Throws
    fun getPostContentTest() {
        val postId = uploadOrEditPost(post("/posts"))
        requestMvc(get("/posts/$postId"))
                .let { post ->
                    val response = mappingResponse(post, PostContentResponse::class.java) as PostContentResponse
                    assert(response.title == "title")
                }

        postRepository.deleteById(postId)
    }


    @Test
    @Throws
    fun uploadPostTest() {
        val post = uploadOrEditPost(post("/posts"))
        postRepository.findById(post)
                .orElseThrow { Exception() }
                .let { assert(post == it.postId) }
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun editPostTest() {
        val post = uploadOrEditPost(post("/posts"), "before_title")

        uploadOrEditPost(patch("/posts/$post"), "after_title")
        postRepository.findById(post)
                .orElseThrow { Exception() }
                .let { assert(it.title == "after_title") }
        postRepository.deleteById(post)
    }


    @Test
    @Throws
    fun deletePostTest() {
        val post = uploadOrEditPost(post("/posts"))
        deletePost(post)
    }


    private fun uploadOrEditPost(method: MockHttpServletRequestBuilder,
                                 title: String = "title",
                                 content: String = "content",
                                 tags: String = "tag, test"
    ): Int {
        val accessToken = accessToken()
        return Integer.parseInt(mvc.perform(
                method
                        .header("Authorization", "Bearer $accessToken")
                        .param("title", title)
                        .param("content", content)
                        .param("tags", tags))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString)
    }


    fun deletePost(postId: Int) {
        val accessToken = accessToken()
        mvc.perform(delete("/posts/$postId")
                .header("Authorization", "Bearer $accessToken"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
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