package sinhee.kang.tutorial.domain.post.domain.subComment

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_sub_comment")
data class SubComment(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var subCommentId: Int = 0,

        @ManyToOne
        @JoinColumn(name = "user")
        var user: User,

        @ManyToOne
        @JoinColumn(name = "comment")
        var comment: Comment,

        @Column(length = 100, nullable = false)
        var content: String,

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now()
)
