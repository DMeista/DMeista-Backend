package sinhee.kang.tutorial.domain.user.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.domain.user.service.userInfo.UserInfoService

@RestController
@RequestMapping("/users")
class UserInfoController(
        private val userInfoService: UserInfoService
) {

    @GetMapping
    fun userInfo(page: Pageable,
                 @RequestParam(required = false) nickname: String?): UserInfoResponse? {
        return userInfoService.getUserInfo(page, nickname)
    }
}