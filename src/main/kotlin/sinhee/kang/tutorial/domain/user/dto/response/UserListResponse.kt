package sinhee.kang.tutorial.domain.user.dto.response

data class UserListResponse (
    var totalItems: Int,

    var applicationResponses: List<UserResponse>
)
