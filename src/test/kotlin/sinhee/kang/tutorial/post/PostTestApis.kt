package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.TestApis

class PostTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
        postId = generatePost(token = currentUserToken)
    }

    @AfterEach
    fun clean() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun uploadPostTest() {
        postRepository.findById(postId)
            .orElseThrow { Exception() }
            .let { assert(postId == it.postId) }
    }

    @Test
    fun editPostTest() {
        generatePost(patch("/posts/$postId"), title = "new title", token = currentUserToken)
        postRepository.findById(postId)
            .orElseThrow { Exception() }
            .let { assert(it.title == "new title") }
    }

    @Test
    fun deletePostTest() {
        mvc.perform(delete("/posts/$postId")
            .header("Authorization", currentUserToken))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
