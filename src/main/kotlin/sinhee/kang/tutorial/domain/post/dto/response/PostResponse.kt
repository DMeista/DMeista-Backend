package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import java.time.LocalDateTime

data class PostResponse (
        var id: Int? = 0,

        var title: String? = "",

        var content: String? = "",

        var author: String? = "",

        var tags: String? = null,

        var viewCount: Int = 0,

        var emojiCount: Int = 0,

        var emoji: EmojiStatus? = null,

        var checked: Boolean = false,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime? = null
)
