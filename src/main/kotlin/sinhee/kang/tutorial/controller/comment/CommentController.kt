package sinhee.kang.tutorial.controller.comment

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.application.dto.request.comment.CommentRequest
import sinhee.kang.tutorial.application.service.comment.CommentService
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication

@RestController
@RequestMapping("/comments")
class CommentController(
    private val commentService: CommentService
) {

    @Authentication
    @PostMapping("/{postId}")
    fun uploadComment(
        @PathVariable postId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int =
        commentService.generateComment(postId, commentRequest)

    @Authentication
    @PatchMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Int,
        @RequestBody commentRequest: CommentRequest
    ): Int =
        commentService.changeComment(commentId, commentRequest)

    @Authentication
    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int) =
        commentService.removeComment(commentId)
}
