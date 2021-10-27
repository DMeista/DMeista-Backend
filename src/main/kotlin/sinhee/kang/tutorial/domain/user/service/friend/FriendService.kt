package sinhee.kang.tutorial.domain.user.service.friend

import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

interface FriendService {
    fun getFriendsList(nickname: String?): UserListResponse

    fun getFriendRequestsList(): UserListResponse

    fun sendFriendRequest(username: String)

    fun acceptFriendRequest(username: String)

    fun removeFriend(username: String)
}
