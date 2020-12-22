package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class PostContentResponse(
        var title: String = "",

        var author: String = "",

        var tags: String? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime? = null,

        var view: Int = 0,

        var content: String = "",

        var isMine: Boolean = false,

        var nextPostTitle: String = "",

        var prePostTitle: String = "",

        var nextPostId: Int? = null,

        var prePostId: Int? = null,

        var images: MutableList<String> = arrayListOf(),

        var comments: MutableList<PostCommentsResponse> = arrayListOf()
)