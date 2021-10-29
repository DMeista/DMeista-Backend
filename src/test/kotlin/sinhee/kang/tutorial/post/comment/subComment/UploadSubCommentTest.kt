package sinhee.kang.tutorial.post.comment.subComment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException

@Suppress("NonAsciiCharacters")
class UploadSubCommentTest : TestProperties() {

    private val testPath = "/comments/sub"

    @BeforeEach
    fun setup() {
        userRepository.save(user)
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
    fun `답글 작성 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))

        val request = CommentRequest("sub-comment")

        val response = requestBody(post("$testPath/${comment.commentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()
        val subComment = subCommentRepository.findById(response)
            .orElseThrow { ApplicationNotFoundException() }

        assert(comment.commentId == subComment.comment.commentId)
    }

    @Test
    fun `댓글을 찾을 수 없음 - NotFound`() {
        val request = CommentRequest("sub-comment")

        requestBody(post("$testPath/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request = CommentRequest("sub-comment")

        requestBody(post("$testPath/0"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
