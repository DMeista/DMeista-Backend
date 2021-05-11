package sinhee.kang.tutorial.domain.auth.domain.emailLimiter.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.EmailLimiter

@Repository
interface EmailLimiterRepository : CrudRepository<EmailLimiter, String>
