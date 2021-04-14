package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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


    @Test
    @Throws
    fun uploadCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment = uploadComment(postId, "Comment Content")

        assert(comment.content == "Comment Content")
    }


    @Test
    @Throws
    fun uploadSubCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment: Comment = uploadComment(postId, "댓글")
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글")

        assert(subComment.content == "대댓글")
    }


    @Test
    @Throws
    fun changeCommentTest() {
        val postId = generatePost(token = accessToken)
        var comment: Comment = uploadComment(postId, "댓글")
        comment = editComment(comment.commentId, "수정된 댓글")

        assert(comment.content == "수정된 댓글")
    }


    @Test
    @Throws
    fun changeSubCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment: Comment = uploadComment(postId, "댓글")
        var subComment: SubComment = uploadSubComment(comment.commentId, "대댓글")
        subComment = editSubComment(subComment.subCommentId, "수정된 대댓글")

        assert(subComment.content == "수정된 대댓글")
    }


    @Test
    @Throws
    fun deleteCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment: Comment = uploadComment(postId, "댓글")
        requestBody(delete("/comments/${comment.commentId}"), token = accessToken)
    }


    @Test
    @Throws
    fun deleteSubCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment: Comment = uploadComment(postId, "댓글")
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글")
        requestBody(delete("/comments/sub/${subComment.subCommentId}"), token = accessToken)
    }


    @Test
    @Throws
    fun deleteCommentWithSubCommentTest() {
        val postId = generatePost(token = accessToken)
        val comment: Comment = uploadComment(postId, "댓글")
        uploadSubComment(comment.commentId, "대댓글")
        requestBody(delete("/comments/${comment.commentId}"), token = accessToken)
    }


    private fun uploadComment(postId: Int?, content: String): Comment {
        requestBody(post("/comments/$postId"), CommentRequest(content), accessToken)
        val post = postRepository.findById(postId!!)
                .orElseThrow { Exception() }
        return post.commentList[0]
    }


    private fun uploadSubComment(commentId: Int, content: String): SubComment {
        requestBody(post("/comments/sub/$commentId"), CommentRequest(content), accessToken)
        val comment = commentRepository.findById(commentId)
                .orElseThrow { Exception() }
        return comment.subCommentList[0]
    }


    private fun editComment(commentId: Int, content: String): Comment {
        requestBody(patch("/comments/$commentId"), CommentRequest(content), accessToken)
        return commentRepository.findById(commentId)
                .orElseThrow { Exception() }
    }


    private fun editSubComment(subCommentId: Int, content: String): SubComment {
        requestBody(patch("/comments/sub/$subCommentId"), CommentRequest(content), accessToken)
        return subCommentRepository.findById(subCommentId)
                .orElseThrow { Exception() }
    }
}
