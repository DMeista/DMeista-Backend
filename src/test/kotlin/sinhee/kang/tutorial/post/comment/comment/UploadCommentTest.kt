package sinhee.kang.tutorial.post.comment.comment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException

@Suppress("NonAsciiCharacters")
class UploadCommentTest: TestProperties() {

    private val testPath = "/comments"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `댓글 작성 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val request = CommentRequest("comment")

        val response = requestBody(post("$testPath/${post.postId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        val comment = commentRepository.findById(response)
            .orElseThrow { ApplicationNotFoundException() }

        assert(post.postId == comment.post.postId)
    }

    @Test
    fun `포스트를 찾을 수 없음 - NotFound`() {
        val request = CommentRequest("comment")

        requestBody(post("$testPath/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val post = postRepository.save(Post(user = user))
        val request = CommentRequest("comment")

        requestBody(post("$testPath/${post.postId}"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
