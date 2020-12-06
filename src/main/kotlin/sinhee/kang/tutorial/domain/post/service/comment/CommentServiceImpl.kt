package sinhee.kang.tutorial.domain.post.service.comment

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.post.exception.ApplicationNotFoundException
import sinhee.kang.tutorial.domain.post.exception.CommentNotFoundException
import sinhee.kang.tutorial.domain.post.exception.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole

@Service
class CommentServiceImpl(
        private var authService: AuthService,

        private var userRepository: UserRepository,
        private var postRepository: PostRepository,
        private var commentRepository: CommentRepository,
        private var subCommentRepository: SubCommentRepository
) : CommentService {

    override fun postComment(postId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        val commentList: MutableList<Comment> = ArrayList()
        commentList.add(
                commentRepository.save(Comment(
                        user = user,
                        post = post,
                        author = user.nickname,
                        content = commentRequest.content,
                        authorType = user.roles
                ))
        )
        postRepository.save(post.addComment(commentList))
        return post.postId!!
    }


    override fun postSubComment(commentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val comment = commentRepository.findById(commentId)
                .orElseThrow{ ApplicationNotFoundException() }
        val subCommentList: MutableList<SubComment> = ArrayList()
        subCommentList.add(
                subCommentRepository.save(SubComment(
                        user = user,
                        comment = comment,
                        author = user.nickname,
                        content = commentRequest.content,
                        authorType = user.roles
                ))
        )
        commentRepository.save(comment.addSubComment(subCommentList))
        return comment.commentId!!
    }


    override fun changeComment(commentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val comment = commentRepository.findById(commentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also {
                    it.content = commentRequest.content
                    commentRepository.save(it)
                }
                ?: { throw PermissionDeniedException() }()
        return comment.commentId!!
    }


    override fun changeSubComment(subCommentId: Int, commentRequest: CommentRequest): Int {
        val user = authService.authValidate()
        val subComment = subCommentRepository.findById(subCommentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also {
                    it.content = commentRequest.content
                    subCommentRepository.save(it)
                }
                ?: { throw PermissionDeniedException() }()
        return subComment.subCommentId!!
    }


    override fun deleteComment(commentId: Int) {
        val user = authService.authValidate()
        commentRepository.findById(commentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also { commentRepository.deleteById(it.commentId!!) }
                ?: { throw PermissionDeniedException() }()
    }


    override fun deleteSubComment(subCommentId: Int) {
        val user = authService.authValidate()
        subCommentRepository.findById(subCommentId)
                .orElseThrow { CommentNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also { subCommentRepository.deleteById(it.subCommentId!!) }
                ?: { throw PermissionDeniedException() }()
    }
}