package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestLib

@Suppress("NonAsciiCharacters")
class UploadPostTest: TestLib() {

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
    fun `포스트 업로드`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
                add("content", "test content")
                add("tags", "tag")
            }

        val response = requestParams(post("/posts"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        postRepository.findById(response)
            .orElseThrow{ NoSuchElementException() }
            .let { assert(it.postId == response) }
    }

    @Test
    fun `잘못된 파라미터 요청`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
            }

        requestParams(post("/posts"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "test title")
                add("content", "test content")
                add("tags", "tag")
            }

        requestParams(post("/posts"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}