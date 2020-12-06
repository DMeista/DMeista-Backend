package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class SignUpRequest(
        @NotEmpty
        @Email
        var email: String,

        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_~])[A-Za-z\\d@$!%*?&_~]{8,}$")
        var password: String,

        @NotEmpty
        var nickname: String
)