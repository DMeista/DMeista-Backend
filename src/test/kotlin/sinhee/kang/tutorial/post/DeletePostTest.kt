package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.post.Post

@Suppress("NonAsciiCharacters")
class DeletePostTest: TestLib() {

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
    fun `포스트 삭제`() {
        val postId = postRepository.save(Post(
            user = user,
            title = "test title",
            content = "test content"
        )).postId

        requestBody(delete("/posts/$postId"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트를 찾을 수 없음`() {
        requestBody(delete("/posts/0"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `삭제할 권한이 없는 경우`() {
        val postId = postRepository.save(Post(
            user = user2,
            title = "test title",
            content = "test content"
        )).postId

        requestBody(delete("/posts/$postId"), currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val postId = postRepository.save(Post(
            user = user2,
            title = "test title",
            content = "test content"
        )).postId

        requestBody(delete("/posts/$postId"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}