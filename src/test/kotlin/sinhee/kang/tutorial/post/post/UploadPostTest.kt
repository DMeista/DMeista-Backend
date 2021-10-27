package sinhee.kang.tutorial.post.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestProperties

@Suppress("NonAsciiCharacters")
class UploadPostTest: TestProperties() {

    private val testPath = "/posts"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `포스트 업로드 - Ok`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
                add("content", "test content")
                add("tags", "tag")
            }

        val response = requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        postRepository.findById(response)
            .orElseThrow{ NoSuchElementException() }
            .let { assert(it.postId == response) }
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
                add("content", "test content")
                add("tags", "tag")
            }

        requestParams(post(testPath), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `잘못된 파라미터 요청 - BadRequest`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
            }

        requestParams(post(testPath), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}