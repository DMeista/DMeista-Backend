package sinhee.kang.tutorial.application.service.user

import sinhee.kang.tutorial.application.dto.response.user.UserInfoResponse

interface UserInfoService {
    fun getUserInfo(nickname: String?): UserInfoResponse?
}
