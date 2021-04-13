package sinhee.kang.tutorial.domain.user.service.friend

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

interface FriendService {
    fun getFriendList(nickname: String?): UserListResponse
    fun receiveFriendRequestList(page: Pageable): UserListResponse?

    fun sendFriendRequest(username: String)
    fun acceptFriendRequest(username: String)
    fun deleteFriend(username: String)
}
