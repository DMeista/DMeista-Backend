package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import java.time.LocalDateTime

class PostCommentsResponse(

        var commentId: Int?,

        var content: String,

        var author: String,

        var authorType: AccountRole,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        var createdAt: LocalDateTime,

        var isMine: Boolean,

        var subComments: MutableList<PostSubCommentsResponse>
)