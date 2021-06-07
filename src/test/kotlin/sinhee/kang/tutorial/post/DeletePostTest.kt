package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment

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
        val postId = postRepository.save(Post(user = user)).postId

        requestBody(delete("/posts/$postId"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트 삭제 시 댓글과 답글 삭제`() {
        val post = postRepository.save(Post(user = user))
        val comment = commentRepository.save(Comment(user = user, post = post, content = "comment"))
        subCommentRepository.save(SubComment(user = user2, comment = comment, content = "sub-comment"))

        requestBody(delete("/posts/${post.postId}"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트를 찾을 수 없음`() {
        requestBody(delete("/posts/0"), token = currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `삭제할 권한이 없는 경우`() {
        val postId = postRepository.save(Post(user = user2)).postId

        requestBody(delete("/posts/$postId"), currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val postId = postRepository.save(Post(user = user2)).postId

        requestBody(delete("/posts/$postId"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}