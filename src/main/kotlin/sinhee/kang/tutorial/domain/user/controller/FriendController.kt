package sinhee.kang.tutorial.domain.user.controller

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.domain.user.service.friend.FriendService

@RestController
@RequestMapping("/users")
class FriendController(
        private val friendService: FriendService
) {

    @GetMapping("/friends")
    fun getFriendsList(pageable: Pageable,
                       @RequestParam(required = false) nickname: String?): UserListResponse? {
        return friendService.getFriendList(nickname)
    }

    @GetMapping("/friends/request")
    fun receiveFriendRequestList(pageable: Pageable): UserListResponse? =
        return friendService.receiveFriendRequestList(pageable)

    @PostMapping("/friends")
    fun sendFriendRequest(@RequestParam username: String) =
        friendService.sendFriendRequest(username)

    @PutMapping("/friends")
    fun acceptFriendRequest(@RequestParam username: String) =
        friendService.acceptFriendRequest(username)

    @DeleteMapping("/friends")
    fun deniedFriendRequest(@RequestParam username: String) =
        friendService.deleteFriend(username)
}
