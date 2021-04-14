package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class PostResponse (
        var id: Int? = 0,

        var title: String? = "",

        var content: String? = "",

        var author: String? = "",

        var tags: String? = null,

        var view: Int = 0,

        var emoji: Int = 0,

        var checked: Boolean = false,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime? = null
)
