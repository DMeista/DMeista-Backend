package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class SignInRequest(
        @Email
        @NotBlank
        var email: String,

        @NotEmpty
        val password: String
)