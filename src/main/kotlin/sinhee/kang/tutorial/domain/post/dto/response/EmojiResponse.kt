package sinhee.kang.tutorial.domain.post.dto.response

import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus

class EmojiResponse(
        var username: String? = null,
        var postId: Int = 0,
        var emojiStatus: EmojiStatus? = null
)