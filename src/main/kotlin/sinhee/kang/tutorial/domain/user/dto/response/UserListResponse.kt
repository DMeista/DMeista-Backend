package sinhee.kang.tutorial.domain.user.dto.response

data class UserListResponse (
        val totalItems: Int,

        val applicationResponses: List<UserResponse>
)
