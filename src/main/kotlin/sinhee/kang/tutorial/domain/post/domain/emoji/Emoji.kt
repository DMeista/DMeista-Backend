package sinhee.kang.tutorial.domain.post.domain.emoji

import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_emoji")
data class Emoji(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var Id: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user")
        var user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post")
        var post: Post,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var status: EmojiStatus? = null,

        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now()
)
