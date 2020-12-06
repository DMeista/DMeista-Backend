package sinhee.kang.tutorial.domain.post.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.post.service.comment.CommentService
import javax.validation.Valid

@RestController
@RequestMapping("/comments")
class CommentController(
        private var commentService: CommentService
) {

    @PostMapping("/{postId}")
    fun uploadComment(@PathVariable postId: Int,
                      @Valid @RequestBody commentRequest: CommentRequest): Int? {
        return commentService.postComment(postId, commentRequest)
    }

    @PatchMapping("/{commentId}")
    fun changeComment(@PathVariable commentId: Int,
                      @Valid @RequestBody commentRequest: CommentRequest): Int? {
        return commentService.changeComment(commentId, commentRequest)
    }

    @DeleteMapping("{commentId}")
    fun deleteComment(@PathVariable commentId: Int) {
        commentService.deleteComment(commentId)
    }

    @PostMapping("/sub/{commentId}")
    fun uploadSubComment(@PathVariable commentId: Int,
                         @Valid @RequestBody commentRequest: CommentRequest): Int {
        return commentService.postSubComment(commentId, commentRequest)
    }

    @PatchMapping("/sub/{subCommentId}")
    fun changeSubComment(@PathVariable subCommentId: Int,
                         @Valid @RequestBody commentRequest: CommentRequest): Int {
        return commentService.changeSubComment(subCommentId, commentRequest)
    }

    @DeleteMapping("/sub/{subCommentId}")
    fun deleteSubComment(@PathVariable subCommentId: Int) {
        commentService.deleteSubComment(subCommentId)
    }
}