package sinhee.kang.tutorial.domain.user.entity.friend

import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.application.dto.response.user.UserResponse
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_friend")
class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: FriendStatus = FriendStatus.REQUEST,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetUser")
    val targetUser: User,

    @Column(nullable = false)
    val connectedAt: LocalDateTime = LocalDateTime.now()

) {
    constructor(user: User, targetUser: User) : this(
        user = user,
        targetUser = targetUser,
        connectedAt = LocalDateTime.now()
    )

    fun updateStatus(targetId: User): Friend =
        apply { status = FriendStatus.ACCEPT }

    fun isStatus(friendStatus: FriendStatus): Boolean =
        status == friendStatus

    fun toUserResponse(user: User): UserResponse =
        UserResponse(this, user)
}
