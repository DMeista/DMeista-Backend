package sinhee.kang.tutorial.application.service.comment

import sinhee.kang.tutorial.application.dto.request.comment.CommentRequest

interface CommentService {
    fun generateComment(postId: Int, commentRequest: CommentRequest): Int

    fun changeComment(commentId: Int, commentRequest: CommentRequest): Int

    fun removeComment(commentId: Int)

    fun uploadSubComment(commentId: Int, commentRequest: CommentRequest): Int

    fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int

    fun removeSubComment(subCommentId: Int)
}
