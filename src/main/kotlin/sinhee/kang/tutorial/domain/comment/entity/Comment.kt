package sinhee.kang.tutorial.domain.comment.entity

import sinhee.kang.tutorial.application.dto.request.comment.CommentRequest
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId: Int = 0,

    @ManyToOne
    @JoinColumn(name = "user")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "post")
    val post: Post,

    @Column(length = 100, nullable = false)
    var content: String = "none",

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL])
    val subCommentList: MutableList<SubComment> = ArrayList()
) {
    constructor(user: User, post: Post, commentRequest: CommentRequest) : this(
        user = user,
        post = post,
        content = commentRequest.content
    )

    fun update(commentRequest: CommentRequest): Comment =
        apply { content = commentRequest.content }
}
