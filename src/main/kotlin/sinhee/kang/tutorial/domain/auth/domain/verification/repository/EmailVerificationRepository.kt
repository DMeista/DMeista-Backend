package sinhee.kang.tutorial.domain.auth.domain.verification.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification

@Repository
interface EmailVerificationRepository : CrudRepository<EmailVerification, String>
