package sinhee.kang.tutorial.application.service.emoji

import sinhee.kang.tutorial.application.dto.response.emoji.EmojiResponse
import sinhee.kang.tutorial.application.dto.response.emoji.PostEmojiListResponse
import sinhee.kang.tutorial.domain.post.entity.enums.EmojiStatus

interface EmojiService {
    fun getPostEmojiUserList(postId: Int): PostEmojiListResponse

    fun setEmoji(postId: Int, emojiStatus: EmojiStatus): EmojiResponse?
}
