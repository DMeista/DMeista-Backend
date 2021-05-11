package sinhee.kang.tutorial.domain.user.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.domain.post.dto.response.PostResponse
import java.time.LocalDateTime

data class UserInfoResponse(
        val username: String = "",

        val email: String = "",

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime? = null,

        val postList: MutableList<PostResponse> = mutableListOf()
)
