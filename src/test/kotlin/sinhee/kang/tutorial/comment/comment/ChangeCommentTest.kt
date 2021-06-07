package sinhee.kang.tutorial.comment.comment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

@Suppress("NonAsciiCharacters")
class ChangeCommentTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        userRepository.save(user2)
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `댓글 변경`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(
            user = user,
            post = post
        ))

        val request = CommentRequest("Changed Comment")

        val response = requestBody(patch("/comments/${comment.commentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

        assert(response == comment.commentId)
    }

    @Test
    fun `댓글을 수정할 권한이 없는 경우`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(
            user = user2,
            post = post
        ))

        val request = CommentRequest("Changed Comment")

        requestBody(patch("/comments/${comment.commentId}"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `댓글을 찾을 수 없는 경우`() {
        val request = CommentRequest("Changed Comment")

        requestBody(patch("/comments/0"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(
            user = user,
            post = post
        ))

        val request = CommentRequest("Changed Comment")

        requestBody(patch("/comments/${comment.commentId}"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}