package sinhee.kang.tutorial.controller.user

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.application.dto.response.user.UserListResponse
import sinhee.kang.tutorial.application.service.friend.FriendService
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication

@RestController
@RequestMapping("/users")
class FriendController(
    private val friendService: FriendService
) {

    @Authentication
    @GetMapping("/friends")
    fun getFriendsList(@RequestParam(required = false) nickname: String?): UserListResponse =
        friendService.getFriendsList(nickname)

    @Authentication
    @GetMapping("/friends/request")
    fun getFriendRequestsList(): UserListResponse =
        friendService.getFriendRequestsList()

    @Authentication
    @PostMapping("/friends")
    fun sendFriendRequest(@RequestParam nickname: String) =
        friendService.sendFriendRequest(nickname)

    @Authentication
    @PutMapping("/friends")
    fun acceptFriendRequest(@RequestParam nickname: String) =
        friendService.acceptFriendRequest(nickname)

    @Authentication
    @DeleteMapping("/friends")
    fun deniedFriendRequest(@RequestParam nickname: String) =
        friendService.removeFriend(nickname)
}
