package sinhee.kang.tutorial.domain.user.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.domain.user.service.userInfo.UserInfoService

@RestController
@RequestMapping("/users")
class UserInfoController(
        private var userInfoService: UserInfoService
) {

    @GetMapping("/{nickname}")
    fun userInfo(page: Pageable,
                 @PathVariable nickname: String): UserInfoResponse? {
        return userInfoService.getUserInfo(page, nickname)
    }
}