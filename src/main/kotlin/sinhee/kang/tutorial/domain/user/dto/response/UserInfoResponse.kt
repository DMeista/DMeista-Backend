package sinhee.kang.tutorial.domain.user.dto.response

import sinhee.kang.tutorial.domain.post.dto.response.PostResponse
import java.time.LocalDateTime

data class UserInfoResponse(
        var username: String,
        var email: String,
        var createdAt: LocalDateTime,

        var postList: MutableList<PostResponse>
)