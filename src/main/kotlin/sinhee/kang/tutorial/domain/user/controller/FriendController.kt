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
    fun getFriendsList(pageable: Pageable,
                       @RequestParam(required = false) nickname: String?): UserListResponse? {
        return friendService.getFriendList(nickname)
    }

    @GetMapping("/friends/request")
    fun receiveFriendRequestList(pageable: Pageable): UserListResponse? {
        return friendService.receiveFriendRequestList(pageable)
    }

    @PostMapping("/friends/{targetId}")
    fun sendFriendRequest(@PathVariable targetId: Int) {
        friendService.sendFriendRequest(targetId)
    }

    @PutMapping("/friends/{targetId}")
    fun acceptFriendRequest(@PathVariable targetId: Int) {
        friendService.acceptFriendRequest(targetId)
    }

    @DeleteMapping("/friends/{targetId}")
    fun deniedFriendRequest(@PathVariable targetId: Int) {
        friendService.deleteFriend(targetId)
    }

}