package sinhee.kang.tutorial.domain.post.dto.response

class PostEmojiListResponse (
        var totalEmoji: Int,
        var applicationResponses: MutableList<EmojiResponse>
)