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
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostApiTest {
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

    @Before
    fun setup() {
        userRepository.save(User(
                email = "rkdtlsgml50@naver.com",
                password = passwordEncoder.encode("1234"),
                nickname = "user"
        ))
    }

    @After
    fun clean() {
        userRepository.findByNickname("user")
                ?.let { user -> userRepository.delete(user) }
    }

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
        val postId = uploadOrEditPost(post("/posts"))
        val post: String = requestMvc(get("/posts/$postId"))
        val response = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostContentResponse::class.java)
        assert(response.title == "title")
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
                .orElseThrow { (Exception()) }
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
        return Integer.parseInt(mvc.perform(method
                .header("Authorization", "Bearer $accessToken")
                .param("title", title)
                .param("content", content)
                .param("tags", tags))
                .andDo(print())
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
        return mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }

    private fun accessToken(): String {
        val content = requestMvc(post("/auth"), SignInRequest("rkdtlsgml50@naver.com", "1234"))
        val response: TokenResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse::class.java)
        return response.accessToken
    }
}