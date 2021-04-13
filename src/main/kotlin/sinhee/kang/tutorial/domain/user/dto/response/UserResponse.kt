package sinhee.kang.tutorial.domain.user.dto.response

import java.time.LocalDateTime

data class UserResponse (
        var id: Int? = 0,

        var nickname: String = "",

        var email: String = "",

        var postContentItems: Int = 0,

        var connectedAt: LocalDateTime
)