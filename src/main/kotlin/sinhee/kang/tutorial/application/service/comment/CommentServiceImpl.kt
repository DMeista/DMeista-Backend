package sinhee.kang.tutorial.application.service.comment

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.application.dto.request.comment.CommentRequest
import sinhee.kang.tutorial.domain.comment.entity.Comment
import sinhee.kang.tutorial.domain.comment.entity.SubComment
import sinhee.kang.tutorial.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.comment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.entity.user.enums.AccountRole
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.UnAuthorizedException

@Service
class CommentServiceImpl(
    private val requestAuthScope: RequestAuthScope,

    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val subCommentRepository: SubCommentRepository
) : CommentService {

    override fun generateComment(postId: Int, commentRequest: CommentRequest): Int {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val comment = commentRepository.save(
            Comment(currentUser, post, commentRequest)
        )

        return comment.commentId
    }

    override fun changeComment(commentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        val comment = commentRepository.findById(commentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply { commentRepository.save(update(commentRequest)) }

        return comment.commentId
    }

    override fun removeComment(commentId: Int) {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        commentRepository.findById(commentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply { commentRepository.deleteById(commentId) }
    }

    override fun uploadSubComment(commentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        val comment = commentRepository.findById(commentId)
            .orElseThrow { ApplicationNotFoundException() }

        val subComment = subCommentRepository.save(
            SubComment(currentUser, comment, commentRequest)
        )

        return subComment.subCommentId
    }

    override fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        val subComment = subCommentRepository.findById(subCommentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)

        return subComment.subCommentId
    }

    override fun removeSubComment(subCommentId: Int) {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        subCommentRepository.findById(subCommentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply { subCommentRepository.deleteById(subCommentId) }
    }

    private fun Comment.checkUserPermission(user: User): Comment =
        takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?: throw PermissionDeniedException()

    private fun SubComment.checkUserPermission(user: User): SubComment =
        takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?: throw PermissionDeniedException()
}
