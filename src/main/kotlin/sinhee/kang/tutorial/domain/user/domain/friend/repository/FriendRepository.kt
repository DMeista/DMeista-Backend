package sinhee.kang.tutorial.domain.user.domain.friend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.user.domain.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.domain.user.User

@Repository
interface FriendRepository: CrudRepository<Friend, Int> {
    fun findByTargetIdAndStatus(targetId: User, status: FriendStatus): MutableList<Friend>?

    fun findByTargetId(pageable: Pageable, targetId: User): Page<Friend>?

    fun findByUserIdAndTargetId(userId: User, targetId: User): Friend?

    fun findByUserIdAndTargetIdAndStatus(userId: User, targetId: User, status: FriendStatus): Friend?
}
