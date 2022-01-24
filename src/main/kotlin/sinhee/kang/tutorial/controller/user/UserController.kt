package sinhee.kang.tutorial.controller.user

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.application.dto.response.user.UserInfoResponse
import sinhee.kang.tutorial.application.service.user.UserInfoService
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication

@RestController
@RequestMapping("/users")
class UserController(
    private val userInfoService: UserInfoService
) {

    @Authentication
    @GetMapping
    fun getUserProfile(@RequestParam(required = false) nickname: String?): UserInfoResponse? =
        userInfoService.getUserInfo(nickname)
}
