package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.transaction.annotation.Transactional

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import javax.servlet.http.Cookie

@Transactional
class CommentApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var subCommentRepository: SubCommentRepository

    private var cookie: Cookie? = null

    private val user: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user",
        password = passwordEncoder.encode("1234")
    )

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        cookie = login(SignInRequest(user.email, "1234"))
    }

    @AfterEach
    fun clean() {
        subCommentRepository.deleteAll()
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun uploadCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment = uploadComment(postId, "Comment Content", cookie)

        assert(comment.content == "Comment Content")
    }

    @Test
    fun uploadSubCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment: Comment = uploadComment(postId, "댓글", cookie)
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", cookie)

        assert(subComment.content == "대댓글")
    }

    @Test
    fun changeCommentTest() {
        val postId = generatePost(cookie = cookie)
        var comment: Comment = uploadComment(postId, "댓글", cookie)
        comment = editComment(comment.commentId, "수정된 댓글", cookie)

        assert(comment.content == "수정된 댓글")
    }

    @Test
    fun changeSubCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment: Comment = uploadComment(postId, "댓글", cookie)
        var subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", cookie)
        subComment = editSubComment(subComment.subCommentId, "수정된 대댓글", cookie)

        assert(subComment.content == "수정된 대댓글")
    }

    @Test
    fun deleteCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment: Comment = uploadComment(postId, "댓글", cookie)
        requestBody(delete("/comments/${comment.commentId}"), cookie = cookie)
    }

    @Test
    fun deleteSubCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment: Comment = uploadComment(postId, "댓글", cookie)
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", cookie)
        requestBody(delete("/comments/sub/${subComment.subCommentId}"), cookie = cookie)
    }

    @Test
    fun deleteCommentWithSubCommentTest() {
        val postId = generatePost(cookie = cookie)
        val comment: Comment = uploadComment(postId, "댓글", cookie)
        uploadSubComment(comment.commentId, "대댓글", cookie)
        requestBody(delete("/comments/${comment.commentId}"), cookie = cookie)
    }

    private fun uploadComment(postId: Int, content: String, cookie: Cookie?): Comment {
        val commentId = requestBody(post("/comments/$postId"), CommentRequest(content), cookie).toInt()
        val post = postRepository.findById(postId)
            .orElseThrow()
        val comment = commentRepository.findById(commentId)
            .orElseThrow()
        post.commentList.add(comment)
        return comment
    }

    private fun uploadSubComment(commentId: Int, content: String, cookie: Cookie?): SubComment {
        val subCommentId = requestBody(post("/comments/sub/$commentId"), CommentRequest(content), cookie).toInt()
        val comment = commentRepository.findById(commentId)
            .orElseThrow()
        val subComment = subCommentRepository.findById(subCommentId)
            .orElseThrow()
        comment.subCommentList.add(subComment)
        return subComment
    }

    private fun editComment(commentId: Int, content: String, cookie: Cookie?): Comment {
        requestBody(patch("/comments/$commentId"), CommentRequest(content), cookie)
        return commentRepository.findById(commentId)
            .orElseThrow { Exception() }
    }

    private fun editSubComment(subCommentId: Int, content: String, cookie: Cookie?): SubComment {
        requestBody(patch("/comments/sub/$subCommentId"), CommentRequest(content), cookie)
        return subCommentRepository.findById(subCommentId)
            .orElseThrow { Exception() }
    }
}
