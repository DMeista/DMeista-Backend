package sinhee.kang.tutorial.domain.post.service.comment

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.global.businessException.exception.post.ApplicationNotFoundException
import sinhee.kang.tutorial.global.businessException.exception.post.CommentNotFoundException
import sinhee.kang.tutorial.global.businessException.exception.auth.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole

@Service
class CommentServiceImpl(
        private val authService: AuthService,

        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val subCommentRepository: SubCommentRepository
) : CommentService {
    override fun uploadComment(postId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }
        val comment = commentRepository.save(Comment(
            user = user,
            post = post,
            content = commentRequest.content
        ))
        return comment.commentId
    }

    override fun changeComment(commentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException() }
            .takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?.also {
                it.content = commentRequest.content
                commentRepository.save(it)
            }
            ?: throw PermissionDeniedException()
        return comment.commentId
    }

    override fun deleteComment(commentId: Int) {
        val user = authService.authValidate()
        commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException() }
            .takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?.also { commentRepository.deleteById(it.commentId) }
            ?: throw PermissionDeniedException()
    }

    override fun uploadSubComment(commentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val comment = commentRepository.findById(commentId)
            .orElseThrow{ ApplicationNotFoundException() }
        val subComment = subCommentRepository.save(SubComment(
            user = user,
            comment = comment,
            content = commentRequest.content
        ))
        return subComment.subCommentId
    }

    override fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val subComment = subCommentRepository.findById(subCommentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
                ?.also {
                    it.content = commentRequest.content
                    subCommentRepository.save(it)
                }
                ?: throw PermissionDeniedException()
        return subComment.subCommentId
    }

    override fun deleteSubComment(subCommentId: Int) {
        val user = authService.authValidate()
        subCommentRepository.findById(subCommentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
                ?.also { subCommentRepository.deleteById(it.subCommentId) }
                ?: throw PermissionDeniedException()
    }
}
