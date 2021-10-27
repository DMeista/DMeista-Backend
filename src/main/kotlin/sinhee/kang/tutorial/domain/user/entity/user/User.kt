package sinhee.kang.tutorial.domain.user.entity.user

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.entity.comment.Comment
import sinhee.kang.tutorial.domain.post.entity.emoji.Emoji
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment
import sinhee.kang.tutorial.domain.post.entity.view.View
import sinhee.kang.tutorial.domain.user.dto.response.UserResponse
import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.entity.user.enums.AccountRole
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(length = 100, unique = true, nullable = false)
    val email: String = "none",

    @Column(length = 20, unique = true, nullable = false)
    val nickname: String = "none",

    @Column(length = 100, nullable = false)
    var password: String = "none",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private val roles: AccountRole = AccountRole.USER,

    @CreatedDate
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var postList: MutableList<Post> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var commentList: MutableList<Comment> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var subCommentList: MutableList<SubComment> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var emojiList: MutableList<Emoji> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var viewList: MutableList<View> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var friendList: MutableList<Friend> = ArrayList()
) {
    fun updatePassword(newPassword: String): User =
        apply { password = newPassword }

    fun isRoles(accountRole: AccountRole): Boolean =
        roles == accountRole
}
