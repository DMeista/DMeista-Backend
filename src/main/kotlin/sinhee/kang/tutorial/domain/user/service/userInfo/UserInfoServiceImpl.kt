package sinhee.kang.tutorial.domain.user.service.userInfo

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.post.dto.response.PostResponse
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException

@Service
class UserInfoServiceImpl(
        private var userRepository: UserRepository
): UserInfoService {

    override fun getUserInfo(pageable: Pageable, nickname: String): UserInfoResponse? {
        val user = userRepository.findByNickname(nickname)
                ?: { throw UserNotFoundException() }()

        val postResponse: MutableList<PostResponse> = ArrayList()
        for(post in user.postList.reversed()) {
            postResponse.add(PostResponse(
                    id = post.postId,
                    title = post.title,
                    content = post.content,
                    author = post.author,
                    view = post.view,
                    createdAt = post.createdAt
            ))
        }
        return UserInfoResponse(user.nickname, user.email, user.createdAt, postResponse)
    }
}