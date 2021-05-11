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
        fun update(content: String): SubComment {
                this.content = content
                return this
        }
}
