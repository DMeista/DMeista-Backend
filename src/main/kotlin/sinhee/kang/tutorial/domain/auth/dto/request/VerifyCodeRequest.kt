package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email

class VerifyCodeRequest (
    @Email
    var email: String,

    var authCode: String
)