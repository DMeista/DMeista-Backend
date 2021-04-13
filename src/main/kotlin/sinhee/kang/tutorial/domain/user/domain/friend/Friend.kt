package sinhee.kang.tutorial.domain.user.domain.friend

import org.springframework.data.annotation.CreatedDate
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tbl_friend")
class Friend(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var status: FriendStatus = FriendStatus.REQUEST,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId")
        var userId: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "targetId")
        var targetId: User,

        @CreatedDate
        @Column(nullable = false)
        var connectedAt: LocalDateTime = LocalDateTime.now()

) {
        fun acceptRequest(targetId: User): Friend {
                status = FriendStatus.ACCEPT
                return this
        }

        fun isAccept(): Boolean {
                return status == FriendStatus.ACCEPT
        }

        fun isRequest(): Boolean {
                return status == FriendStatus.REQUEST
        }
}
