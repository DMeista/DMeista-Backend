package sinhee.kang.tutorial.domain.post.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import java.time.LocalDateTime

data class PostSubCommentsResponse(
        val subCommentId: Int?,

        val content: String,

        val author: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime,

        val isMine: Boolean
)
