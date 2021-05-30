package sinhee.kang.tutorial.domain.user.service.userInfo

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.domain.view.repository.ViewRepository
import sinhee.kang.tutorial.domain.post.dto.response.PostResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

@Service
class UserInfoServiceImpl(
    private val userRepository: UserRepository,
    private val viewRepository: ViewRepository,
    private val authService: AuthService
): UserInfoService {

    override fun getUserInfo(pageable: Pageable, nickname: String?): UserInfoResponse? {
        val user: User = nickname
            ?.let { userRepository.findByNickname(it)
                ?: throw UserNotFoundException() }
            ?: run { authService.verifyCurrentUser() }

        val postResponse: MutableList<PostResponse> = generatePostResponse(user)

        return UserInfoResponse(
            username = user.nickname,
            email = user.email,
            createdAt = user.createdAt,
            postList = postResponse
        )
    }

    private fun generatePostResponse(user: User): MutableList<PostResponse> {
        val postResponse: MutableList<PostResponse> = ArrayList()
        user.postList
            .reversed()
            .forEach { post ->
                postResponse.add(PostResponse(
                    id = post.postId,
                    title = post.title,
                    content = post.content,
                    author = post.user.nickname,
                    viewCount = viewRepository.findByPost(post).count(),
                    emojiCount = post.emojiList.count(),
                    emoji = post.emojiList
                        .filter { emoji -> emoji.user == user }
                        .map { it.status }.firstOrNull(),
                    createdAt = post.createdAt
                ))
            }
        return postResponse
    }
}
