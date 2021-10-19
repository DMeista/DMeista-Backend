package sinhee.kang.tutorial.domain.auth.repository.requestLimiter

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.entity.requestLimiter.EmailRequestLimiter

@Repository
interface EmailRequestLimiterRepository : CrudRepository<EmailRequestLimiter, String>
