package sinhee.kang.tutorial.application.dto.response.emoji

import sinhee.kang.tutorial.domain.post.entity.Post

data class PostEmojiListResponse(
    val totalEmoji: Int = 0,

    val applications: MutableList<EmojiResponse> = arrayListOf()
) {
    constructor(post: Post) : this(
        totalEmoji = post.emojiList.count(),
        applications = getEmojiLists(post)
    )

    companion object {
        private fun getEmojiLists(post: Post): MutableList<EmojiResponse> =
            mutableListOf<EmojiResponse>().apply {
                post.emojiList.forEach { emoji ->
                    add(EmojiResponse(emoji))
                }
            }
    }
}
