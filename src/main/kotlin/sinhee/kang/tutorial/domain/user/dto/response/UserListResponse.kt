package sinhee.kang.tutorial.domain.user.dto.response

class UserListResponse (
    var totalItems: Int,

    var applicationResponses: MutableList<UserResponse>
)