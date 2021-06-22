package sinhee.kang.tutorial.domain.auth.repository.emailLimiter

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.entity.emailLimiter.EmailRequestLimiter

@Repository
interface EmailRequestLimiterRepository : CrudRepository<EmailRequestLimiter, String>
