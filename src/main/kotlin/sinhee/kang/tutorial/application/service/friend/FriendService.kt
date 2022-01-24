package sinhee.kang.tutorial.application.service.friend

import sinhee.kang.tutorial.application.dto.response.user.UserListResponse

interface FriendService {
    fun getFriendsList(nickname: String?): UserListResponse

    fun getFriendRequestsList(): UserListResponse

    fun sendFriendRequest(username: String)

    fun acceptFriendRequest(username: String)

    fun removeFriend(username: String)
}
