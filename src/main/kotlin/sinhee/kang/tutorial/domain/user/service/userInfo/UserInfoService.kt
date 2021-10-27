package sinhee.kang.tutorial.domain.user.service.userInfo

import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

interface UserInfoService {
    fun getUserInfo(nickname: String?): UserInfoResponse?
}
