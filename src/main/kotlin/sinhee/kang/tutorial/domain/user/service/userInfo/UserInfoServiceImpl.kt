package sinhee.kang.tutorial.domain.user.service.userInfo

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.domain.view.repository.ViewRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.global.security.exception.UserNotFoundException

@Service
class UserInfoServiceImpl(
        private val userRepository: UserRepository,
        private val viewRepository: ViewRepository,
        private val authService: AuthService
): UserInfoService {

    override fun getUserInfo(pageable: Pageable, nickname: String?): UserInfoResponse? {
        lateinit var user: User
        nickname
            ?.let {
                user = userRepository.findByNickname(nickname)
                    ?: throw UserNotFoundException()
            }
            ?: run {
                user = authService.authValidate()
            }

        val postResponse: MutableList<PostResponse> = ArrayList()

        user.postList.reversed()
            .forEach { post ->
                val checkedUser = viewRepository.findByPost(post)
                postResponse.add(PostResponse(
                    id = post.postId,
                    title = post.title,
                    content = post.content,
                    author = post.user.nickname,
                    viewCount = checkedUser.count(),
                    emojiCount = post.emojiList.count(),
                    emoji = post.emojiList
                        .filter { emoji -> emoji.user == user }
                        .map { it.status }.firstOrNull(),
                    createdAt = post.createdAt
                ))
            }

        return UserInfoResponse(user.nickname, user.email, user.createdAt, postResponse)
    }
}
