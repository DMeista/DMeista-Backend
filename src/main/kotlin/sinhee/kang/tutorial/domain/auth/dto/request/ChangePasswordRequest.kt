package sinhee.kang.tutorial.domain.auth.dto.request

import sinhee.kang.tutorial.global.util.validator.Validator
import javax.validation.constraints.NotBlank

data class ChangePasswordRequest (
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val newPassword: String
) {
    init {
        with(Validator) {
            email(email)
            password(password)
            password(newPassword)
        }
    }
}
