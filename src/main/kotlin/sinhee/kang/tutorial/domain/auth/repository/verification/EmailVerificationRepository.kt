package sinhee.kang.tutorial.domain.auth.repository.verification

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.entity.verification.EmailVerification
import java.util.*

@Repository
interface EmailVerificationRepository : CrudRepository<EmailVerification, UUID> {
    fun findByEmail(email: String): EmailVerification?
}
