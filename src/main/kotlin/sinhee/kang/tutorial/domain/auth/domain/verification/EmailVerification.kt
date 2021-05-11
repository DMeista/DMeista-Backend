package sinhee.kang.tutorial.domain.auth.domain.verification

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus

@RedisHash(timeToLive = 60 * 3)
data class EmailVerification(
        @Id
        val email: String,

        val authCode: String,

        var status: EmailVerificationStatus = EmailVerificationStatus.UNVERIFIED,

        @TimeToLive
        var ttl: Long = 60L
) {
    fun verify(): EmailVerification {
        status = EmailVerificationStatus.VERIFIED
        ttl *= 3
        return this
    }

    fun isVerify(): Boolean =
        status == EmailVerificationStatus.VERIFIED
}
