package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TokenType
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class CommentApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var subCommentRepository: SubCommentRepository

    private val user: User = User(
        nickname = "user",
        email = "rkdtlsgml40@dsm.hs.kr",
        password = passwordEncoder.encode("1234")
    )

    private lateinit var accessToken: String

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        accessToken = "Bearer ${getToken(TokenType.ACCESS, user.email, "1234")}"
    }

    @AfterEach
    fun clean() {
        subCommentRepository.deleteAll()
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }



}
