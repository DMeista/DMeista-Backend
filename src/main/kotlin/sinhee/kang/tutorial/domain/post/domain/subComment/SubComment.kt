package sinhee.kang.tutorial.domain.post.domain.subComment

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_sub_comment")
class SubComment(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var subCommentId: Int? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user")
        var user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "comment")
        var comment: Comment,

        @Column(length = 100, nullable = false)
        var content: String,

        @Column(nullable = false)
        var author: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var authorType: AccountRole,

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime
) {
        constructor(user: User, comment: Comment, content: String, author: String, authorType: AccountRole): this(
                user = user,
                comment = comment,
                content = content,
                author = author,
                authorType = authorType,
                createdAt = LocalDateTime.now()
        )
}