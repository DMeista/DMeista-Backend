package sinhee.kang.tutorial.domain.user.domain.friend

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.dto.response.UserResponse
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_friend")
data class Friend(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var status: FriendStatus = FriendStatus.REQUEST,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId")
        val userId: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "targetId")
        val targetId: User,

        @CreatedDate
        @Column(nullable = false)
        val connectedAt: LocalDateTime = LocalDateTime.now()

) {
        fun acceptRequest(targetId: User): Friend {
                status = FriendStatus.ACCEPT
                return this
        }

        fun isAccept(): Boolean =
                status == FriendStatus.ACCEPT

        fun isRequest(): Boolean =
                status == FriendStatus.REQUEST

        fun toUserResponse(user: User): UserResponse =
                UserResponse(
                        id = user.id,
                        nickname = user.nickname,
                        email = user.email,
                        postContentItems = user.postList.count(),
                        connectedAt = connectedAt
                )
}
