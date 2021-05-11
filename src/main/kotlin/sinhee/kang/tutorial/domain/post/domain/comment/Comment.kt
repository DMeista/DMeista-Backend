package sinhee.kang.tutorial.domain.post.domain.comment

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_comment")
data class Comment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val commentId: Int = 0,

        @ManyToOne
        @JoinColumn(name = "user")
        val user: User,

        @ManyToOne
        @JoinColumn(name = "post")
        val post: Post,

        @Column(length = 100, nullable = false)
        var content: String = "none",

        @CreatedDate
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL])
        val subCommentList: MutableList<SubComment> = ArrayList()
) {
        fun update(content: String): Comment {
                this.content = content
                return this
        }
}
