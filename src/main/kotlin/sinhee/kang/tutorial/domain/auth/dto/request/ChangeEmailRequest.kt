package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class ChangeEmailRequest (

    @Email
    @NotEmpty
    var beforeEmail: String,

    @Email
    @NotEmpty
    var afterEmail: String
)