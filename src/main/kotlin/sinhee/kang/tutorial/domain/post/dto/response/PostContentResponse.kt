package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class PostContentResponse(
        var title: String,

        var author: String,

        var tags: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime,

        var view: Int,

        var content: String,

        var isMine: Boolean,

        var nextPostTitle: String,

        var prePostTitle: String,

        var nextPostId: Int?,

        var prePostId: Int?,

        var comments: MutableList<PostCommentsResponse>
)