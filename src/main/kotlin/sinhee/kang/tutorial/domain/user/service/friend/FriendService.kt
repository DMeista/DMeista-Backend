package sinhee.kang.tutorial.domain.user.service.friend

import org.springframework.data.domain.Pageable
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

interface FriendService {
    fun getFriendList(nickname: String): UserListResponse
    fun receiveFriendRequestList(page: Pageable): UserListResponse?

    fun sendFriendRequest(userId: Int)
    fun acceptFriendRequest(userId: Int)
    fun deleteFriend(userId: Int)
}