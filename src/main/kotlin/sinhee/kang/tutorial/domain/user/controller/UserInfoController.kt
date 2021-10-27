package sinhee.kang.tutorial.domain.user.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.domain.user.service.userInfo.UserInfoService

@RestController
@RequestMapping("/users")
class UserInfoController(
    private val userInfoService: UserInfoService
) {

    @GetMapping
    fun getUserProfile(@RequestParam(required = false) nickname: String?): UserInfoResponse? =
        userInfoService.getUserInfo(nickname)
}
