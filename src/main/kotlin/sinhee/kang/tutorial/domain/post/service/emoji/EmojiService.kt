package sinhee.kang.tutorial.domain.post.service.emoji

import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse

interface EmojiService {
    fun getPostEmojiUserList(postId: Int): PostEmojiListResponse

    fun setEmoji(postId: Int, emojiStatus: EmojiStatus): EmojiResponse?
}
