package sinhee.kang.tutorial.domain.user.service.userInfo

import org.springframework.data.domain.Pageable
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

interface UserInfoService {
    fun getUserInfo(pageable: Pageable, nickname: String?): UserInfoResponse?
}
