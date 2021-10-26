package sinhee.kang.tutorial.domain.user.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.domain.user.service.friend.FriendService

@RestController
@RequestMapping("/users")
class FriendController(
    private val friendService: FriendService
) {

    @GetMapping("/friends")
    fun getFriendsList(@RequestParam(required = false) nickname: String?): UserListResponse =
        friendService.getFriendsList(nickname)

    @GetMapping("/friends/request")
    fun getFriendRequestsList(): UserListResponse =
        friendService.getFriendRequestsList()

    @PostMapping("/friends")
    fun sendFriendRequest(@RequestParam nickname: String) =
        friendService.sendFriendRequest(nickname)

    @PutMapping("/friends")
    fun acceptFriendRequest(@RequestParam nickname: String) =
        friendService.acceptFriendRequest(nickname)

    @DeleteMapping("/friends")
    fun deniedFriendRequest(@RequestParam nickname: String) =
        friendService.removeFriend(nickname)
}
