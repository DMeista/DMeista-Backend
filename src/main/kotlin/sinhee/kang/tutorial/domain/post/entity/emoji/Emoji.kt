package sinhee.kang.tutorial.domain.post.entity.emoji

import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_emoji")
class Emoji(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    val post: Post,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: EmojiStatus? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(user: User, post: Post, status: EmojiStatus?) : this(
        user = user,
        post = post,
        status = status,
        createdAt = LocalDateTime.now()
    )

    fun update(emojiStatus: EmojiStatus): Emoji =
        apply { status = emojiStatus }
}
