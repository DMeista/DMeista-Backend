package sinhee.kang.tutorial.domain.user.domain.user

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
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
        var roles: AccountRole,

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime,

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var postList: MutableList<Post> = ArrayList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var commentList: MutableList<Comment> = ArrayList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var subCommentList: MutableList<SubComment> = ArrayList(),

        @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var friendList: MutableList<Friend> = ArrayList()
) {
    constructor() : this(
            email = "",
            nickname = "",
            password = "",
            roles = AccountRole.USER,
            createdAt = LocalDateTime.now()
    )

    fun addPost(post: Post): User {
        val postList: MutableList<Post> = ArrayList()
        postList.add(post)
        this.postList = postList
        return this
    }

    fun addComment(comment: MutableList<Comment>): User {
        commentList = comment
        return this
    }

    fun addSubComment(subComment: MutableList<SubComment>): User {
        subCommentList = subComment
        return this
    }

    fun addFriend(friend: Friend): User {
        val friendList: MutableList<Friend> = ArrayList()
        friendList.add(friend)
        this.friendList = friendList
        return this
    }
}