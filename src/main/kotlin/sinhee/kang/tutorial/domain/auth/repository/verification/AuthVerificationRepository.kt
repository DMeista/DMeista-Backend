package sinhee.kang.tutorial.domain.auth.repository.verification

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.entity.verification.AuthVerification

@Repository
interface AuthVerificationRepository : CrudRepository<AuthVerification, String>
