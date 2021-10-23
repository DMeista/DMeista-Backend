package sinhee.kang.tutorial.domain.user.service.userInfo

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

@Service
class UserInfoServiceImpl(
    private val authService: AuthService,

    private val userRepository: UserRepository
): UserInfoService {

    override fun getUserInfo(nickname: String?): UserInfoResponse? =
        UserInfoResponse(getUserByNickname(nickname))

    private fun getUserByNickname(nickname: String?): User =
        if (nickname.isNullOrEmpty())
            authService.getCurrentUser()
        else userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException()
}
