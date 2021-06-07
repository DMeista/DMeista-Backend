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
    fun uploadComment(@PathVariable postId: Int,
                      @RequestBody commentRequest: CommentRequest): Int {
        return commentService.uploadComment(postId, commentRequest)
    }

    @PatchMapping("/{commentId}")
    fun changeComment(@PathVariable commentId: Int,
                      @RequestBody commentRequest: CommentRequest): Int {
        return commentService.updateComment(commentId, commentRequest)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int) {
        commentService.removeComment(commentId)
    }

    @PostMapping("/sub/{commentId}")
    fun uploadSubComment(@PathVariable commentId: Int,
                         @RequestBody commentRequest: CommentRequest): Int {
        return commentService.uploadSubComment(commentId, commentRequest)
    }

    @PatchMapping("/sub/{subCommentId}")
    fun changeSubComment(@PathVariable subCommentId: Int,
                         @RequestBody commentRequest: CommentRequest): Int {
        return commentService.updateSubComment(subCommentId, commentRequest)
    }

    @DeleteMapping("/sub/{subCommentId}")
    fun deleteSubComment(@PathVariable subCommentId: Int) {
        commentService.removeSubComment(subCommentId)
    }
}
