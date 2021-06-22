package sinhee.kang.tutorial.domain.auth.entity.verification

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.domain.auth.entity.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidAuthCodeException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException

@RedisHash(timeToLive = 60 * 3)
data class AuthVerification(
    @Id
    val email: String,

    val authCode: String,

    var emailStatus: EmailVerificationStatus = EmailVerificationStatus.UNVERIFIED,

    var nickname: String? = null,

    @TimeToLive
    var ttl: Long = 60L
) {
    private fun setVerify(): AuthVerification {
        emailStatus = EmailVerificationStatus.VERIFIED
        ttl = 60 * 5
        return this
    }

    fun checkAuthCode(code: String): AuthVerification {
        if (authCode != code)
            throw InvalidAuthCodeException()
        else setVerify()
        return this
    }

    fun checkEqualNickname(nickname: String?): AuthVerification {
        if (this.nickname != nickname || this.nickname == null)
            throw BadRequestException()
        return this
    }

    fun isVerify(): Boolean =
        emailStatus == EmailVerificationStatus.VERIFIED
}
