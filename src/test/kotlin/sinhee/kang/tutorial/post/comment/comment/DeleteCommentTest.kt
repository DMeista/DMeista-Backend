package sinhee.kang.tutorial.post.comment.comment

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestFactory
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment
import sinhee.kang.tutorial.domain.user.entity.user.User

@Suppress("NonAsciiCharacters")
class DeleteCommentTest: TestFactory() {

    private val testPath = "/comments"

    @BeforeEach
    fun setup() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()

        userRepository.apply {
            save(user)
            save(user2)
        }
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `댓글 삭제 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))

        requestBody(delete("$testPath/${comment.commentId}"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `댓글 삭제 삭제시 답글 삭제 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))
        subCommentRepository.save(SubComment(user = user2, comment = comment, content = "sub-comment"))

        requestBody(delete("$testPath/${comment.commentId}"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `댓글을 찾을 수 없음 - NotFound`() {
        requestBody(delete("$testPath/0"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `댓글 삭제 권한이 없는 경우 - Unauthorized`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user2, post = post, content = "comment"))

        requestBody(delete("$testPath/${comment.commentId}"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        requestBody(delete("$testPath/0"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
