package sinhee.kang.tutorial.domain.user.repository.friend

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface FriendRepository: CrudRepository<Friend, Int> {
    fun findByUserAndStatus(targetId: User, status: FriendStatus): List<Friend>

    fun findByTargetUserAndStatus(targetId: User, status: FriendStatus): List<Friend>

    fun findByUserAndTargetUserAndStatus(userId: User, targetId: User, status: FriendStatus = FriendStatus.REQUEST): Friend?

    fun findByUserOrTargetUserAndTargetUserOrUser(user: User, targetUser: User, targetUser2: User, user2: User): Friend?

    fun existsByUserAndTargetUser(user: User, targetUser: User): Boolean

    fun existsByUserAndTargetUserAndStatus(user: User, targetUser: User, status: FriendStatus): Boolean
}
