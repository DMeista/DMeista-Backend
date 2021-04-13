package sinhee.kang.tutorial.domain.post.dto.response

data class PostEmojiListResponse (
        var totalEmoji: Int = 0,
        var applicationResponses: MutableList<EmojiResponse> = arrayListOf()
)
