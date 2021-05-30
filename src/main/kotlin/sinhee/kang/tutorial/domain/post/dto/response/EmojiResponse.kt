package sinhee.kang.tutorial.domain.post.dto.response

import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus

data class EmojiResponse(
    val username: String? = null,

    val postId: Int? = 0,

    val emojiStatus: EmojiStatus? = null
)
