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
import sinhee.kang.tutorial.global.businessException.exception.common.BadRequestException
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException

@Service
class FriendServiceImpl(
    private val authService: AuthService,

    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
): FriendService {

    override fun getFriendsList(nickname: String?): UserListResponse {
        val user: User = nickname
            ?.let {
                userRepository.findByNickname(it)
                ?: throw UserNotFoundException() }
            ?: run { authService.authValidate() }

        val userResponses: MutableList<UserResponse> = ArrayList()

        user.friendList
            .filter(Friend::isAccept)
            .forEach {
                userResponses.add(UserResponse(
                    id = it.targetId.id,
                    nickname = it.targetId.nickname,
                    email = it.targetId.email,
                    postContentItems = it.targetId.postList.count(),
                    connectedAt = it.connectedAt
                ))
            }
        friendRepository.findByTargetIdAndStatus(user, FriendStatus.ACCEPT)
            ?.forEach {
                userResponses.add(UserResponse(
                    id = it.userId.id,
                    nickname = it.userId.nickname,
                    email = it.userId.email,
                    postContentItems = it.userId.postList.count(),
                    connectedAt = it.connectedAt
                ))
            }
        userResponses.sortBy { it.connectedAt }

        return UserListResponse(userResponses.count(), userResponses)
    }

    override fun getFriendRequestsList(page: Pageable): UserListResponse? {
        val user = authService.authValidate()

        val userResponses: MutableList<UserResponse> = mutableListOf()

        friendRepository.findByTargetId(page, user)
            ?.filter(Friend::isRequest)?.toList()
            ?.forEach {
                userResponses.add(UserResponse(
                    id = it.userId.id,
                    nickname = it.userId.nickname,
                    email = it.userId.email,
                    postContentItems = it.userId.postList.count(),
                    connectedAt = it.connectedAt
                ))
            }
            ?: throw UserNotFoundException()

        return UserListResponse(userResponses.count(), userResponses)
    }

    override fun sendFriendRequest(username: String) {
        val user = authService.authValidate()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        if (user == targetUser || isConnection(user, targetUser))
            throw BadRequestException()

        friendRepository.save(Friend(
            userId = user,
            targetId = targetUser,
            status = FriendStatus.REQUEST
        ))
    }

    override fun acceptFriendRequest(username: String) {
        val user = authService.authValidate()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        val userRequest = friendRepository.findByUserIdAndTargetIdAndStatus(
                userId = targetUser, targetId = user, status = FriendStatus.REQUEST)
            ?: throw UserNotFoundException()

        friendRepository.save(userRequest.acceptRequest(user))
        userRepository.save(user.addFriend(userRequest))
    }

    override fun removeFriend(username: String) {
        val user = authService.authValidate()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        when {
            user.isExistUserAndTargetUser(targetUser) -> {
                friendRepository.findByUserIdAndTargetId(userId = user, targetId = targetUser)
                    ?.let { friendRepository.delete(it) }
            }
            targetUser.isExistUserAndTargetUser(user) -> {
                friendRepository.findByUserIdAndTargetId(userId = targetUser, targetId = user)
                    ?.let { friendRepository.delete(it) }
            }
            else -> throw BadRequestException()
        }
    }

    private fun isConnection(user1: User, user2: User): Boolean {
        val connection1 = user1.isExistUserAndTargetUser(user2)
        val connection2 = user2.isExistUserAndTargetUser(user1)

        return connection1 && connection2
    }

    private fun User.isExistUserAndTargetUser(targetUser: User) =
        friendRepository.findByUserIdAndTargetId(this, targetUser)
            ?.let { true }
            ?: false
}
