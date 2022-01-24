package sinhee.kang.tutorial.controller.comment

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.application.dto.request.comment.CommentRequest
import sinhee.kang.tutorial.application.service.comment.CommentService
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication

@RestController
@RequestMapping("/comments/sub")
class SubCommentController(
    private val commentService: CommentService
) {

    @Authentication
    @PostMapping("/{commentId}")
    fun uploadSubComment(
        @PathVariable commentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int =
        commentService.uploadSubComment(commentId, commentRequest)

    @Authentication
    @PatchMapping("{subCommentId}")
    fun updateSubComment(
        @PathVariable subCommentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int =
        commentService.changeSubComment(subCommentId, commentRequest)

    @Authentication
    @DeleteMapping("{subCommentId}")
    fun deleteSubComment(@PathVariable subCommentId: Int) =
        commentService.removeSubComment(subCommentId)
}