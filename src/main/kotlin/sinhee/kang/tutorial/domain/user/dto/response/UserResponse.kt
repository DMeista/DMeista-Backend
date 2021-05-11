package sinhee.kang.tutorial.domain.user.dto.response

import java.time.LocalDateTime

data class UserResponse (
        val id: Int? = 0,

        val nickname: String = "",

        val email: String = "",

        val postContentItems: Int = 0,

        val connectedAt: LocalDateTime
)
