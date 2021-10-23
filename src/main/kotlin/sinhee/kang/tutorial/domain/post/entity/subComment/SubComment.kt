package sinhee.kang.tutorial.domain.post.entity.subComment

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_sub_comment")
class SubComment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val subCommentId: Int = 0,

    @ManyToOne
    @JoinColumn(name = "user")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "comment")
    val comment: Comment,

    @Column(length = 100, nullable = false)
    var content: String,

    @CreatedDate
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(user: User, comment: Comment, commentRequest: CommentRequest): this(
        user = user,
        comment = comment,
        content = commentRequest.content
    )

    fun update(commentRequest: CommentRequest): SubComment =
        apply { content = commentRequest.content }
}
