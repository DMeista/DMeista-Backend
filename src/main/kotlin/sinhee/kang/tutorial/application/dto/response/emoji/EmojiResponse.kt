package sinhee.kang.tutorial.application.dto.response.emoji

import sinhee.kang.tutorial.domain.post.entity.Emoji
import sinhee.kang.tutorial.domain.post.entity.enums.EmojiStatus

data class EmojiResponse(
    val postId: Int? = 0,

    val username: String? = null,

    val emojiStatus: EmojiStatus? = null
) {
    constructor(emoji: Emoji) : this(
        postId = emoji.post.postId,
        username = emoji.user.nickname,
        emojiStatus = emoji.status
    )
}
