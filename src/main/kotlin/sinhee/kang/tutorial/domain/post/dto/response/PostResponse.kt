package sinhee.kang.tutorial.domain.post.dto.response

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

        var createdAt: LocalDateTime? = null
)