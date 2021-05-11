package sinhee.kang.tutorial.domain.post.service.comment

import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

interface CommentService {
    fun uploadComment(postId: Int, commentRequest: CommentRequest): Int

    fun updateComment(commentId: Int, commentRequest: CommentRequest): Int

    fun removeComment(commentId: Int)

    fun uploadSubComment(commentId: Int, commentRequest: CommentRequest): Int

    fun updateSubComment(subCommentId: Int, commentRequest: CommentRequest): Int

    fun removeSubComment(subCommentId: Int)
}
