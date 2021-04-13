package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SignUpRequest(
        @NotBlank
        @Email
        var email: String,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_~])[A-Za-z\\d@$!%*?&_~]{8,}$")
        var password: String,

        @NotBlank
        var nickname: String
)
