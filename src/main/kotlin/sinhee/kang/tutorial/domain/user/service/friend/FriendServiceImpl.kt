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
import sinhee.kang.tutorial.global.config.security.exception.BadRequestException
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import kotlin.collections.ArrayList

@Service
class FriendServiceImpl(
        private var authService: AuthService,
        private var userRepository: UserRepository,
        private var friendRepository: FriendRepository

): FriendService {

    override fun getFriendList(nickname: String): UserListResponse {
        val user = userRepository.findByNickname(nickname)
                ?: { throw UserNotFoundException() }()
        val userResponse: MutableList<UserResponse> = ArrayList()
        for (users in user.friendList.filter { it.isAccept() }) {
            userResponse.add(UserResponse(
                    id = users.targetId.id,
                    nickname = users.targetId.nickname,
                    email = users.targetId.email,
                    postContentItems = users.targetId.postList.count(),
                    connectedAt = users.connectedAt
            ))
        }
        friendRepository.findByTargetIdAndStatus(user, FriendStatus.ACCEPT)
                ?.let {
                    for (users in it){
                        userResponse.add(UserResponse(
                                id = users.userId.id,
                                nickname = users.userId.nickname,
                                email = users.userId.email,
                                postContentItems = users.userId.postList.count(),
                                connectedAt = users.connectedAt
                        ))
                    }
                }
        userResponse.sortBy { it.connectedAt }

        return UserListResponse(
                totalItems = userResponse.count(),
                applicationResponses = userResponse
        )
    }


    override fun receiveFriendRequestList(page: Pageable): UserListResponse? {
        val user = authService.authValidate()
        val requestList = friendRepository.findByTargetId(page, user)
                ?.filter { it.isRequest() }
                ?:{ throw UserNotFoundException() }()
        val userResponse: MutableList<UserResponse> = ArrayList()

        for (userRequest in requestList){
            userResponse.add(UserResponse(
                    id = userRequest.userId.id,
                    nickname = userRequest.userId.nickname,
                    email = userRequest.userId.email,
                    postContentItems = userRequest.userId.postList.count(),
                    connectedAt = userRequest.connectedAt
            ))
        }
        return UserListResponse(
                totalItems = requestList.count(),
                applicationResponses = userResponse
        )
    }


    override fun sendFriendRequest(userId: Int) {
        val user = authService.authValidate()
        val targetUser = userRepository.findById(userId)
                .orElseThrow{ BadRequestException() }

        if (isCheckUserAndTargetUserExist(user, targetUser)
                || isCheckUserAndTargetUserExist(user = targetUser, targetUser = user)
                || user == targetUser){ throw BadRequestException() }
        friendRepository.save(Friend(
                userId = user,
                targetId = targetUser,
                status = FriendStatus.REQUEST
        ))
    }


    override fun acceptFriendRequest(userId: Int) {
        val targetUser = authService.authValidate()
        val user = userRepository.findById(userId)
                .orElseThrow { UserNotFoundException() }

        val userRequest = friendRepository.findByUserIdAndTargetIdAndStatus(userId = user, targetId =  targetUser, status =  FriendStatus.REQUEST)
                ?: { throw UserNotFoundException() }()
        friendRepository.save(userRequest.acceptRequest(user))
        userRepository.save(user.addFriend(userRequest))
    }


    override fun deleteFriend(userId: Int) {
        val targetUser = authService.authValidate()
        val user = userRepository.findById(userId)
                .orElseThrow { UserNotFoundException() }
        when {
            isCheckUserAndTargetUserExist(user, targetUser) -> {
                friendRepository.findByUserIdAndTargetId(user, targetUser)
                        ?.let { friendRepository.delete(it) }
            }
            isCheckUserAndTargetUserExist(user = targetUser, targetUser = user) -> {
                friendRepository.findByUserIdAndTargetId(userId = targetUser, targetId = user)
                        ?.let { friendRepository.delete(it) }
            }
            else -> throw BadRequestException()
        }
    }


    fun isCheckUserAndTargetUserExist(user: User, targetUser: User): Boolean {
        return friendRepository.findByUserIdAndTargetId(user, targetUser)
                ?.let { true }
                ?:{ false }()
    }

}
