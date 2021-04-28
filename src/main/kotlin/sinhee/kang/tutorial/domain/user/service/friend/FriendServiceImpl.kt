package sinhee.kang.tutorial.domain.user.service.friend

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.domain.user.dto.response.UserResponse
import sinhee.kang.tutorial.global.security.exception.BadRequestException
import sinhee.kang.tutorial.global.security.exception.UserNotFoundException

@Service
class FriendServiceImpl(
        private val authService: AuthService,
        private val userRepository: UserRepository,
        private val friendRepository: FriendRepository

): FriendService {
    override fun getFriendList(nickname: String?): UserListResponse {
        lateinit var user: User
        nickname
            ?.let {
                user = userRepository.findByNickname(it)
                    ?: throw UserNotFoundException()
            }
            ?: run {
                user = authService.authValidate()
            }

        val userResponse = user.friendList
            .filter { it.isAccept() }
            .toMutableList()
            .addUserResponse()
            .apply { sortBy { it.connectedAt } }

        return UserListResponse(
                totalItems = userResponse.count(),
                applicationResponses = userResponse
        )
    }

    override fun receiveFriendRequestList(page: Pageable): UserListResponse? {
        val user = authService.authValidate()
        val requestList = friendRepository.findByTargetId(page, user)
                ?.filter { it.isRequest() }
                ?: throw UserNotFoundException()
        val userResponse = requestList.toMutableList().addUserResponse()

        return UserListResponse(
                totalItems = requestList.count(),
                applicationResponses = userResponse
        )
    }

    override fun sendFriendRequest(username: String) {
        val user = authService.authValidate()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        if (isCheckUserAndTargetUserExist(user, targetUser)
                || isCheckUserAndTargetUserExist(user = targetUser, targetUser = user)
                || user == targetUser){ throw BadRequestException() }
        friendRepository.save(Friend(
                userId = user,
                targetId = targetUser,
                status = FriendStatus.REQUEST
        ))
    }

    override fun acceptFriendRequest(username: String) {
        val targetUser = authService.authValidate()
        val user = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        val userRequest = friendRepository.findByUserIdAndTargetIdAndStatus(userId = user, targetId = targetUser, status = FriendStatus.REQUEST)
                ?: throw UserNotFoundException()
        friendRepository.save(userRequest.acceptRequest(user))
        userRepository.save(user.addFriend(userRequest))
    }

    override fun deleteFriend(username: String) {
        val user = authService.authValidate()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()
        when {
            isCheckUserAndTargetUserExist(user = user, targetUser = targetUser) -> {
                friendRepository.findByUserIdAndTargetId(userId = user, targetId = targetUser)
                        ?.let { friendRepository.delete(it) }
            }
            isCheckUserAndTargetUserExist(user = targetUser, targetUser = user) -> {
                friendRepository.findByUserIdAndTargetId(userId = targetUser, targetId = user)
                        ?.let { friendRepository.delete(it) }
            }
            else -> throw BadRequestException()
        }
    }

    private fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?: false
    }

    private fun MutableList<Friend>.addUserResponse(): MutableList<UserResponse> {
        val userResponses: MutableList<UserResponse> = mutableListOf()
        this.forEach {
            userResponses.add(UserResponse(
                id = it.userId.id,
                nickname = it.userId.nickname,
                email = it.userId.email,
                postContentItems = it.userId.postList.count(),
                connectedAt = it.connectedAt
            ))
        }
        return userResponses
    }
}

