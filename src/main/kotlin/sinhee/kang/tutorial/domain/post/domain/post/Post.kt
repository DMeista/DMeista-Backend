package sinhee.kang.tutorial.domain.post.domain.post

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.file.domain.ImageFile
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_post")
class Post(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var postId: Int? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user")
        var user: User? = null,

        @Column(length = 50, nullable = false)
        var title: String = "none",

        @Column(columnDefinition = "TEXT", length = 1600, nullable = false)
        var content: String = "none",

        @Column(length = 10, unique = true, nullable = false)
        var author: String = "none",

        @Column
        var tags: String? = "",

        @Column(nullable = false)
        var view: Int = 0,

        @CreatedDate
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var commentList: MutableList<Comment> = ArrayList(),

        @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var likeUserList: MutableList<User> = ArrayList(),

        @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        var imageFileList: MutableList<ImageFile> = ArrayList()


) {
    constructor(user: User, title: String, content: String, author: String, tags: String?): this(
            user = user,
            title = title,
            content = content,
            author = author,
            tags = tags,
            createdAt = LocalDateTime.now()
    )

    fun addLikeUser(user: User): Post {
        likeUserList.add(user)
        return this
    }

    fun deleteUser(user: User): Post {
        likeUserList.remove(user)
        return this
    }

    fun view(): Post {
        view++
        return this
    }
}