package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import java.time.LocalDateTime

data class PostResponse (
        val id: Int? = 0,

        val title: String? = "",

        val content: String? = "",

        val author: String? = "",

        val tags: String? = null,

        val viewCount: Int = 0,

        val emojiCount: Int = 0,

        val emoji: EmojiStatus? = null,

        val checked: Boolean = false,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime? = null
)
