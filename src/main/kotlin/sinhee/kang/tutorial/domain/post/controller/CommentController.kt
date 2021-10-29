package sinhee.kang.tutorial.domain.post.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.post.service.comment.CommentService

@RestController
@RequestMapping("/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/{postId}")
    fun uploadComment(
        @PathVariable postId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int {
        return commentService.generateComment(postId, commentRequest)
    }

    @PatchMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int {
        return commentService.changeComment(commentId, commentRequest)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int) {
        commentService.removeComment(commentId)
    }

    @PostMapping("/sub/{commentId}")
    fun uploadSubComment(
        @PathVariable commentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int {
        return commentService.uploadSubComment(commentId, commentRequest)
    }

    @PatchMapping("/sub/{subCommentId}")
    fun updateSubComment(
        @PathVariable subCommentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int {
        return commentService.changeSubComment(subCommentId, commentRequest)
    }

    @DeleteMapping("/sub/{subCommentId}")
    fun deleteSubComment(@PathVariable subCommentId: Int) {
        commentService.removeSubComment(subCommentId)
    }
}
