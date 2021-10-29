package sinhee.kang.tutorial.domain.user.dto.response

data class UserListResponse(
    val totalItems: Int,

    val applications: List<UserResponse>
) {
    constructor(friends: List<UserResponse>) : this(
        totalItems = friends.count(),
        applications = friends
    )
}
