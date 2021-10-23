package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime

data class PostCommentsResponse(
    val commentId: Int?,

    val content: String,

    val author: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,

    val isMine: Boolean,

    val subComments: MutableList<PostSubCommentsResponse>
) {
    constructor(user: User?, comment: Comment): this(
        commentId = comment.commentId,
        content = comment.content,
        createdAt = comment.createdAt,
        author = comment.user.nickname,
        isMine = (comment.user == user),
        subComments = getCommentSubComment(user, comment)
    )

    companion object {
        private fun getCommentSubComment(user: User?, comment: Comment): MutableList<PostSubCommentsResponse> =
            mutableListOf<PostSubCommentsResponse>().apply {
                comment.subCommentList.forEach { subComment ->
                    add(PostSubCommentsResponse(user, subComment))
                }
            }
    }
}
