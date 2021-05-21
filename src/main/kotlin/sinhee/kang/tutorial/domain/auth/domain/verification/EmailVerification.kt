package sinhee.kang.tutorial.domain.auth.domain.verification

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidAuthCodeException

@RedisHash(timeToLive = 60 * 3)
data class EmailVerification(
        @Id
        val email: String,

        val authCode: String,

        var status: EmailVerificationStatus = EmailVerificationStatus.UNVERIFIED,

        @TimeToLive
        var ttl: Long = 60L
) {
    fun setVerify(): EmailVerification {
        status = EmailVerificationStatus.VERIFIED
        ttl *= 3
        return this
    }

    fun checkAuthCode(code: String): EmailVerification {
        if (authCode != code)
            throw InvalidAuthCodeException()
        return this
    }

    fun isVerify(): Boolean =
        status == EmailVerificationStatus.VERIFIED
}
