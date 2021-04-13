package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class EmailRequest(
        @Email
        @NotBlank
        var email: String
)
