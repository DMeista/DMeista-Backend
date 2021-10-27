package sinhee.kang.tutorial.post.comment.subComment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

@Suppress("NonAsciiCharacters")
class ChangeSubCommentTest: TestProperties() {

    private val testPath = "/comments/sub"

    @BeforeEach
    fun setup() {
        userRepository.apply {
            save(user)
            save(user2)
        }
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        subCommentRepository.deleteAll()
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `답글 변경 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))
        val subComment = subCommentRepository.save(SubComment(user = user, comment = comment, content = "sub-comment"))

        val request = CommentRequest("Changed SubComment")

        requestBody(patch("$testPath/${subComment.subCommentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `답글을 찾을 수 없는 경우 - NotFound`() {
        val request = CommentRequest("Changed SubComment")

        requestBody(patch("$testPath/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `답글을 수정할 권한이 없는 경우 - Unauthorized`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))
        val subComment = subCommentRepository.save(SubComment(user = user2, comment = comment, content = "sub-comment"))

        val request = CommentRequest("Changed SubComment")

        requestBody(patch("$testPath/${subComment.subCommentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request = CommentRequest("Changed SubComment")

        requestBody(patch("$testPath/0"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
