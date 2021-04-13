package sinhee.kang.tutorial.domain.post.service.emoji

import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse

interface EmojiService {
    fun emojiService(postId: Int, status: EmojiStatus): EmojiResponse?
    fun getPostEmojiUserList(postId: Int): PostEmojiListResponse
}
