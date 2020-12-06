package sinhee.kang.tutorial.domain.auth.domain.verification

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus

@RedisHash(timeToLive = 60 * 3)
class EmailVerification(
        val MINUTE: Long = 60L,

        @Id
        var email: String,

        var authCode: String,

        var status: EmailVerificationStatus = EmailVerificationStatus.UNVERIFID,

        @TimeToLive
        var ttl: Long = 0
) {
    fun verify() {
        this.status = EmailVerificationStatus.VERIFIED
        this.ttl = 3 * MINUTE
    }
    fun setUnVerify(): EmailVerification {
        this.status = EmailVerificationStatus.UNVERIFID
        return this
    }

    fun isVerify(): Boolean {
        return status == EmailVerificationStatus.VERIFIED
    }
}