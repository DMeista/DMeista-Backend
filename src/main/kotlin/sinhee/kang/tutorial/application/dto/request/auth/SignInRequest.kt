package sinhee.kang.tutorial.application.dto.request.auth

import sinhee.kang.tutorial.infra.util.validator.Validator
import javax.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
) {
    init {
        with(sinhee.kang.tutorial.infra.util.validator.Validator) {
            email(email)
            password(password)
        }
    }
}
