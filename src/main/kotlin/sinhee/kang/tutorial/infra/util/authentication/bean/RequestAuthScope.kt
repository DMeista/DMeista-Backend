package sinhee.kang.tutorial.infra.util.authentication.bean

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import sinhee.kang.tutorial.domain.user.entity.user.User

@Component
@RequestScope
data class RequestAuthScope(
    var user: User? = null
)
