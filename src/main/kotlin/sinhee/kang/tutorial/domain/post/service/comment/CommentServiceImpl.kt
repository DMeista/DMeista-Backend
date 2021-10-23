package sinhee.kang.tutorial.domain.post.service.comment

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.repository.comment.CommentRepository
import sinhee.kang.tutorial.domain.post.repository.post.PostRepository
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment
import sinhee.kang.tutorial.domain.post.repository.subComment.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.entity.user.enums.AccountRole

@Service
class CommentServiceImpl(
    private val authService: AuthService,

    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val subCommentRepository: SubCommentRepository
): CommentService {

    override fun generateComment(postId: Int, commentRequest: CommentRequest): Int {
        val currentUser = authService.getCurrentUser()
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val comment = commentRepository.save(
            Comment(currentUser, post, commentRequest)
        )

        return comment.commentId
    }

    override fun changeComment(commentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = authService.getCurrentUser()

        val comment = commentRepository.findById(commentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply { commentRepository.save(update(commentRequest)) }

        return comment.commentId
    }

    override fun removeComment(commentId: Int) {
        val currentUser = authService.getCurrentUser()

        commentRepository.findById(commentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply { commentRepository.deleteById(commentId) }
    }

    override fun uploadSubComment(commentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = authService.getCurrentUser()
        val comment = commentRepository.findById(commentId)
            .orElseThrow{ ApplicationNotFoundException() }

        val subComment = subCommentRepository.save(
            SubComment(currentUser, comment, commentRequest)
        )

        return subComment.subCommentId
    }

    override fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int {
        val currentUser = authService.getCurrentUser()

        val subComment = subCommentRepository.findById(subCommentId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)

        return subComment.subCommentId
    }

    override fun removeSubComment(subCommentId: Int) {
        val currentUser = authService.getCurrentUser()

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
