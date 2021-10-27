package sinhee.kang.tutorial.post.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestFactory
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment

@Suppress("NonAsciiCharacters")
class DeletePostTest: TestFactory() {

    private val testPath = "/posts"

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
    fun `포스트 삭제 - Ok`() {
        val postId = postRepository.save(Post(user = user)).postId

        requestBody(delete("$testPath/$postId"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트 삭제 시 댓글과 답글 삭제 - Ok`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))
        subCommentRepository.save(SubComment(user = user2, comment = comment, content = "sub-comment"))

        requestBody(delete("$testPath/${post.postId}"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트를 찾을 수 없음 - NotFound`() {
        requestBody(delete("$testPath/0"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `삭제할 권한이 없는 경우 - Unauthorized`() {
        val postId = postRepository.save(Post(user = user2)).postId

        requestBody(delete("$testPath/$postId"), currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val postId = postRepository.save(Post(user = user2)).postId

        requestBody(delete("$testPath/$postId"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}