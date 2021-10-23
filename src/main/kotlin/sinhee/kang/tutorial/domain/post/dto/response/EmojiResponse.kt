package sinhee.kang.tutorial.domain.post.dto.response

import sinhee.kang.tutorial.domain.post.entity.emoji.Emoji
import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.user.entity.user.User

data class EmojiResponse(
    val postId: Int? = 0,

    val username: String? = null,

    val emojiStatus: EmojiStatus? = null
) {
    constructor(emoji: Emoji): this(
        postId = emoji.post.postId,
        username = emoji.user.nickname,
        emojiStatus = emoji.status
    )
}
