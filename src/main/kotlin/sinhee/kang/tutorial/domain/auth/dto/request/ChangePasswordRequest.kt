package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class ChangePasswordRequest (

    @Email
    @NotBlank
    val email: String,

    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @NotEmpty
    var password: String

)