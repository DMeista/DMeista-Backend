package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException

@Suppress("NonAsciiCharacters")
class EditPostTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        userRepository.save(user2)
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `포스트 수정`() {
        val postId = postRepository.save(Post(
            user = user,
            title = "before",
            content = "before Content"
        )).postId
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {
                add("title", "after")
                add("content", "after content")
            }

        val response = requestParams(patch("/posts/$postId"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        postRepository.findById(response)
            .orElseThrow{ BadRequestException() }
            .let { assert(it.title == "after") }
    }

    @Test
    fun `포스트를 찾을 수 없는 경우`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("title", "after") }

        requestParams(patch("/posts/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `수정할 권한이 없는 경우`() {
        val postId = postRepository.save(Post(
            user = user2,
            title = "before",
            content = "before Content"
        )).postId

        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("title", "after") }

        requestParams(patch("/posts/$postId"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val postId = postRepository.save(Post(
            user = user,
            title = "before",
            content = "before Content"
        )).postId
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("title", "after") }

        requestParams(patch("/posts/$postId"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}