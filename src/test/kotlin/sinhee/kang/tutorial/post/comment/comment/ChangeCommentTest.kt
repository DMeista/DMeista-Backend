package sinhee.kang.tutorial.post.comment.comment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestFactory
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

@Suppress("NonAsciiCharacters")
class ChangeCommentTest: TestFactory() {

    private val testPath = "/comments"

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
        userRepository.apply {
            save(user)
            save(user2)
        }
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        postRepository.deleteAll()
        commentRepository.deleteAll()
    }

    @Test
    fun `댓글 변경 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user, post, CommentRequest("Changed Comment")))

        val request = CommentRequest("Changed Comment")

        val response = requestBody(patch("$testPath/${comment.commentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        assert(response == comment.commentId)
    }

    @Test
    fun `댓글을 찾을 수 없는 경우 - NotFound`() {
        val request = CommentRequest("Changed Comment")

        requestBody(patch("$testPath/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `댓글을 수정할 권한이 없는 경우 - Unauthorized`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user2, post, CommentRequest("Comment")))

        val request = CommentRequest("Changed Comment")

        requestBody(patch("$testPath/${comment.commentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request = CommentRequest("Changed Comment")

        requestBody(patch("$testPath/0"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
