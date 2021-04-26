package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import java.time.LocalDateTime

data class PostContentResponse(
        var title: String = "",

        var author: String = "",

        var tags: String? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime? = null,

        var viewCount: Int = 0,

        var emojiCount: Int = 0,

        var emoji: EmojiStatus? = null,

        var content: String = "",

        var isMine: Boolean = false,

        var nextPostTitle: String = "",

        var prevPostTitle: String = "",

        var nextPostId: Int? = null,

        var prevPostId: Int? = null,

        var images: List<String> = arrayListOf(),

        var comments: List<PostCommentsResponse> = arrayListOf()
)
