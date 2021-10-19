package sinhee.kang.tutorial.domain.auth.entity.verification

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Id

@RedisHash
class EmailVerification(
    @Id
    val id: UUID,

    @Indexed
    val email: String,

    private val authCode: String,

    private var expired: Boolean = false,

    private var lastUpdatedAt: LocalDateTime = LocalDateTime.now(),

    @TimeToLive
    private var ttl: Long = 60L * 5L
) {

    fun setExpired(): EmailVerification =
        apply { if (!expired) expired = true }

    fun updateLiveCycle(): EmailVerification =
        apply { if (expired) ttl = 60L * 5L }

    fun updateAccessTime(): EmailVerification =
        apply { lastUpdatedAt = LocalDateTime.now() }

    fun isCorrectAuthCode(code: String): Boolean =
        (code == authCode && !expired)

    fun isConfirmation(): Boolean = expired
}
