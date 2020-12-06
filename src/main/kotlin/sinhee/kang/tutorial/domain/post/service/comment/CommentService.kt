package sinhee.kang.tutorial.domain.post.service.comment

import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest

interface CommentService {
    fun postComment(postId: Int, commentRequest: CommentRequest): Int
    fun changeComment(commentId: Int, commentRequest: CommentRequest): Int
    fun deleteComment(commentId: Int)

    fun postSubComment(commentId: Int, commentRequest: CommentRequest): Int
    fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int
    fun deleteSubComment(subCommentId: Int)
}