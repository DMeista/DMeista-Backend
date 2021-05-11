package sinhee.kang.tutorial.domain.user.service.friend

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse

interface FriendService {
    fun getFriendsList(nickname: String?): UserListResponse

    fun getFriendRequestsList(page: Pageable): UserListResponse?

    fun sendFriendRequest(username: String)

    fun acceptFriendRequest(username: String)

    fun removeFriend(username: String)
}
