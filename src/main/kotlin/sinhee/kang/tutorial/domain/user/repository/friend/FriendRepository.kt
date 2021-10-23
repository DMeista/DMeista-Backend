package sinhee.kang.tutorial.domain.user.repository.friend

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.user.entity.friend.Friend
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface FriendRepository: CrudRepository<Friend, Int> {
    fun findByTargetUserAndStatus(targetId: User, status: FriendStatus): MutableList<Friend>?

    fun findByTargetUser(pageable: Pageable, targetId: User): Page<Friend>?

    fun findByUserAndTargetUser(userId: User, targetId: User): Friend?

    fun findByUserAndTargetUserAndStatus(userId: User, targetId: User, status: FriendStatus): Friend?
}
