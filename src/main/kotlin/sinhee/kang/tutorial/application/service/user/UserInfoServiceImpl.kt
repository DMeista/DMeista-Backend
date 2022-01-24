package sinhee.kang.tutorial.application.service.user

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.application.dto.response.user.UserInfoResponse
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.UserNotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.UnAuthorizedException

@Service
class UserInfoServiceImpl(
    private val requestAuthScope: RequestAuthScope,

    private val userRepository: UserRepository
) : UserInfoService {

    override fun getUserInfo(nickname: String?): UserInfoResponse? =
        UserInfoResponse(getUserByNickname(nickname))

    private fun getUserByNickname(nickname: String?): User =
        if (nickname.isNullOrEmpty())
            requestAuthScope.user
                ?: throw UnAuthorizedException()
        else userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException()
}
