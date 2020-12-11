package sinhee.kang.tutorial.domain.post.dto.response

import sinhee.kang.tutorial.domain.user.domain.user.User

data class UserLikeResponse(
        var totalLike: Int = 0,
        var userList: MutableList<User>? = null
)