package sinhee.kang.tutorial.domain.user.domain.user

import org.springframework.data.annotation.CreatedDate
import org.springframework.security.crypto.password.PasswordEncoder
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.view.View
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.global.businessException.exception.auth.IncorrectPasswordException
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    fun addFriend(friend: Friend): User {
        this.friendList.add(friend)
        return this
    }

    fun isRoles(roles: AccountRole): Boolean{
        return this.roles == roles
    }

    fun isMatchedPassword(passwordEncoder: PasswordEncoder, password: String) {
        if (!passwordEncoder.matches(password, this.password))
            throw IncorrectPasswordException()
    }
}
