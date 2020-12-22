package sinhee.kang.tutorial.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.assertj.core.api.ArraySortedAssert
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
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
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @Throws
    fun getAllPostListTest() {
        val post: String = mvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val response: PostListResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostListResponse::class.java)
        assert(response.totalItems == 4)
    }


    @Test
    @Throws
    fun getTagPostListTest() {
        val post: String = mvc.perform(get("/posts?tags=안경"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val response: PostListResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostListResponse::class.java)
        assert(response.totalItems == 2)
    }


    @Test
    @Throws
    fun getPostContentTest() {
        val post: String = mvc.perform(get("/posts/78"))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val response: PostContentResponse = objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(post, PostContentResponse::class.java)
        assert(response.title == "타이틀")
    }

    @Test
    @Throws
    fun uploadPostTest() {
        
    }


    @Throws
    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any) {
        mvc.perform(method
                .content(ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn()
    }
}