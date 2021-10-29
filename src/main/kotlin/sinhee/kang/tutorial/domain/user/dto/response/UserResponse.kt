package sinhee.kang.tutorial.domain.user.dto.response

import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Int? = 0,

    val nickname: String = "",

    val email: String = "",

    val postCount: Int = 0,

    private val connectedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(friend: Friend, user: User) : this(
        id = user.id,
        nickname = user.nickname,
        email = user.email,
        postCount = user.postList.count(),
        connectedAt = friend.connectedAt
    )
}
