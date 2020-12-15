package sinhee.kang.tutorial.domain.user.domain.user

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_user")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        @Column(length = 100, unique = true, nullable = false)
        var email: String,

        @Column(length = 20, unique = true, nullable = false)
        var nickname: String,

        @Column(length = 100, nullable = false)
        var password: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private var roles: AccountRole = AccountRole.USER,

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var postList: MutableList<Post> = ArrayList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var commentList: MutableList<Comment> = ArrayList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var subCommentList: MutableList<SubComment> = ArrayList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var emojiList: MutableList<Emoji> = ArrayList(),

        @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var friendList: MutableList<Friend> = ArrayList()
) {
    constructor() : this(
            email = "",
            nickname = "",
            password = ""
    )

    fun addFriend(friend: Friend): User {
        this.friendList.add(friend)
        return this
    }

    fun isRoles(roles: AccountRole): Boolean{
        return this.roles == roles
    }
}