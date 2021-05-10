package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.transaction.annotation.Transactional

import sinhee.kang.tutorial.TestApis
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

@Transactional
class CommentTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
        postId = generatePost(token = currentUserToken)
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
        val comment = uploadComment(postId, "Comment Content", currentUserToken)

        assert(comment.content == "Comment Content")
    }

    @Test
    fun uploadSubCommentTest() {
        val comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", currentUserToken)

        assert(subComment.content == "대댓글")
    }

    @Test
    fun changeCommentTest() {
        var comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        comment = editComment(comment.commentId, "수정된 댓글", currentUserToken)

        assert(comment.content == "수정된 댓글")
    }

    @Test
    fun changeSubCommentTest() {
        val comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        var subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", currentUserToken)
        subComment = editSubComment(subComment.subCommentId, "수정된 대댓글", currentUserToken)

        assert(subComment.content == "수정된 대댓글")
    }

    @Test
    fun deleteCommentTest() {
        val comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        requestBody(delete("/comments/${comment.commentId}"), token = currentUserToken)
    }

    @Test
    fun deleteSubCommentTest() {
        val comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글", currentUserToken)
        requestBody(delete("/comments/sub/${subComment.subCommentId}"), token = currentUserToken)
    }

    @Test
    fun deleteCommentWithSubCommentTest() {
        val comment: Comment = uploadComment(postId, "댓글", currentUserToken)
        uploadSubComment(comment.commentId, "대댓글", currentUserToken)
        requestBody(delete("/comments/${comment.commentId}"), token = currentUserToken)
    }

    private fun uploadComment(postId: Int, content: String, token: String?): Comment {
        val commentId = requestBody(post("/comments/$postId"), CommentRequest(content), token).toInt()
        val post = postRepository.findById(postId)
            .orElseThrow()
        val comment = commentRepository.findById(commentId)
            .orElseThrow()
        post.commentList.add(comment)
        return comment
    }

    private fun uploadSubComment(commentId: Int, content: String, token: String?): SubComment {
        val subCommentId = requestBody(post("/comments/sub/$commentId"), CommentRequest(content), token).toInt()
        val comment = commentRepository.findById(commentId)
            .orElseThrow()
        val subComment = subCommentRepository.findById(subCommentId)
            .orElseThrow()
        comment.subCommentList.add(subComment)
        return subComment
    }

    private fun editComment(commentId: Int, content: String, token: String?): Comment {
        requestBody(patch("/comments/$commentId"), CommentRequest(content), token)
        return commentRepository.findById(commentId)
            .orElseThrow { Exception() }
    }

    private fun editSubComment(subCommentId: Int, content: String, token: String?): SubComment {
        requestBody(patch("/comments/sub/$subCommentId"), CommentRequest(content), token)
        return subCommentRepository.findById(subCommentId)
            .orElseThrow { Exception() }
    }
}
