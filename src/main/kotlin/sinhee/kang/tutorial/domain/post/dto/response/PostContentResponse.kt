package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import java.time.LocalDateTime

data class PostContentResponse(
    val title: String = "",

    val author: String = "",

    val tags: String? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime? = null,

    val viewCount: Int = 0,

    val emojiCount: Int = 0,

    val emoji: EmojiStatus? = null,

    val content: String = "",

    val isMine: Boolean = false,

    val nextPostTitle: String = "",

    val prevPostTitle: String = "",

    val nextPostId: Int? = null,

    val prevPostId: Int? = null,

    val images: List<String> = arrayListOf(),

    val comments: List<PostCommentsResponse> = arrayListOf()
)
