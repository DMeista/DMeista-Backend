package sinhee.kang.tutorial.domain.auth.domain.verification

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.global.businessException.exception.auth.InvalidAuthCodeException
import sinhee.kang.tutorial.global.businessException.exception.common.BadRequestException

@RedisHash(timeToLive = 60 * 3)
data class SignUpVerification(
    @Id
    val email: String,

    val authCode: String,

    var emailStatus: EmailVerificationStatus = EmailVerificationStatus.UNVERIFIED,

    var nickname: String? = null,

    @TimeToLive
    var ttl: Long = 60L
) {
    private fun setVerify(): SignUpVerification {
        emailStatus = EmailVerificationStatus.VERIFIED
        ttl = 60 * 5
        return this
    }

    fun checkAuthCode(code: String): SignUpVerification {
        if (authCode != code)
            throw InvalidAuthCodeException()
        else setVerify()
        return this
    }

    fun checkEqualNickname(nickname: String?): SignUpVerification {
        if (this.nickname != nickname || this.nickname == null)
            throw BadRequestException()
        return this
    }

    fun isVerify(): Boolean =
        emailStatus == EmailVerificationStatus.VERIFIED
}
