package sinhee.kang.tutorial.domain.user.service.friend

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus.ACCEPT
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus.REQUEST
import sinhee.kang.tutorial.domain.user.dto.response.UserListResponse
import sinhee.kang.tutorial.domain.user.dto.response.UserResponse
import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.friend.FriendRepository
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

@Service
class FriendServiceImpl(
    private val authService: AuthService,

    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
) : FriendService {

    override fun getFriendsList(nickname: String?): UserListResponse {
        val user = getUserByNickname(nickname)

        val acceptRequests = mutableListOf<UserResponse>().apply {
            with(friendRepository) {
                findByUserAndStatus(user, ACCEPT)
                    .forEach { add(it.toUserResponse(it.targetUser)) }
                findByTargetUserAndStatus(user, ACCEPT)
                    .forEach { add(it.toUserResponse(it.user)) }
            }
        }

        return UserListResponse(acceptRequests)
    }

    override fun getFriendRequestsList(): UserListResponse {
        val currentUser = authService.getCurrentUser()

        val userResponses = mutableListOf<UserResponse>().apply {
            friendRepository.findByTargetUserAndStatus(currentUser, REQUEST)
                .forEach { add(it.toUserResponse(it.user)) }
        }

        return UserListResponse(userResponses)
    }

    override fun sendFriendRequest(username: String) {
        val currentUser = authService.getCurrentUser()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        if (isConnect(currentUser, targetUser) || currentUser == targetUser)
            throw BadRequestException()

        friendRepository.save(Friend(currentUser, targetUser))
    }

    override fun acceptFriendRequest(username: String) {
        val currentUser = authService.getCurrentUser()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        val friend = friendRepository.findByUserAndTargetUserAndStatus(userId = targetUser, targetId = currentUser)
            ?: throw UserNotFoundException()

        friendRepository.save(friend.updateStatus(currentUser))
    }

    override fun removeFriend(username: String) {
        val currentUser = authService.getCurrentUser()
        val targetUser = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()

        if (!isConnect(currentUser, targetUser))
            throw BadRequestException()

        deleteConnection(currentUser, targetUser)
    }

    private fun getUserByNickname(nickname: String?): User =
        if (nickname.isNullOrEmpty())
            authService.getCurrentUser()
        else userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException()

    private fun deleteConnection(user1: User, user2: User) {
        if (isConnect(user1, user2))
            friendRepository.apply {
                findByUserOrTargetUserAndTargetUserOrUser(user1, user1, user2, user2)
                    ?.let { delete(it) }
            }
    }

    private fun isConnect(user1: User, user2: User): Boolean =
        friendRepository.existsByUserAndTargetUser(user1, user2) ||
            friendRepository.existsByUserAndTargetUser(user2, user1)
}
