package sinhee.kang.tutorial.domain.post.dto.response

data class PostEmojiListResponse (
        val totalEmoji: Int = 0,

        val applicationResponses: MutableList<EmojiResponse> = arrayListOf()
)
