package sinhee.kang.tutorial.domain.post.domain.view

import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_view")
class View (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var Id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = true)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post", nullable = true)
    var post: Post,

    @Column(nullable = false)
    var createdAt: LocalDateTime
) {
    constructor(user: User, post: Post): this(
            user = user,
            post = post,
            createdAt = LocalDateTime.now()
    )
}