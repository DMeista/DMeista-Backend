package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email

class EmailRequest(
        @Email
        var email: String
)