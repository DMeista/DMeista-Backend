package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import java.time.LocalDateTime

data class PostCommentsResponse(

        var commentId: Int?,

        var content: String,

        var author: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime,

        var isMine: Boolean,

        var subComments: MutableList<PostSubCommentsResponse>
)