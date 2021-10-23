package sinhee.kang.tutorial.domain.auth.dto.request

import sinhee.kang.tutorial.global.util.validator.Validator
import javax.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
) {
    init {
        with(Validator) {
            email(email)
            password(password)
        }
    }
}
