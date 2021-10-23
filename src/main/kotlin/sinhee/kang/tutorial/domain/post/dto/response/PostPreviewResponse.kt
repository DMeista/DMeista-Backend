package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime

data class PostPreviewResponse(
    val id: Int? = 0,

    val title: String? = "",

    val content: String? = "",

    val author: String? = "",

    val tags: String? = null,

    val viewCount: Int = 0,

    val emojiCount: Int = 0,

    val emoji: EmojiStatus? = null,

    val checked: Boolean = false,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime? = null
) {
    constructor(user: User?, post: Post): this(
        id = post.postId,
        title = post.title,
        content = post.content,
        author = post.user.nickname,
        tags = post.tags,
        viewCount = post.viewList.count(),
        emojiCount = post.emojiList.count(),
        emoji = post.emojiList
            .filter { emoji -> emoji.user == user }
            .map { it.status }.firstOrNull(),
        checked = post.viewList
            .any { view -> view.user == user },
        createdAt = post.createdAt
    )
}
