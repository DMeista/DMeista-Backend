package sinhee.kang.tutorial.domain.post.domain.comment

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_comment")
class Comment(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var commentId: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user")
        var user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post")
        var post: Post,

        @Column(length = 100, nullable = false)
        var content: String = "none",

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var subCommentList: MutableList<SubComment> = ArrayList()

) {
        fun addSubComment(subComment: MutableList<SubComment>): Comment {
                subCommentList = subComment
                return this
        }
}
