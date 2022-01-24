package sinhee.kang.tutorial.application.dto.response.user

data class UserListResponse(
    val totalItems: Int = 0,

    val applications: List<UserResponse> = mutableListOf()
) {
    constructor(friends: List<UserResponse>) : this(
        totalItems = friends.count(),
        applications = friends
    )
}
