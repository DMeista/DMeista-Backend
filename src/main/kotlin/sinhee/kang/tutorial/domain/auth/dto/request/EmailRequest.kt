package sinhee.kang.tutorial.domain.auth.dto.request

import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import sinhee.kang.tutorial.global.util.validator.Validator
import javax.validation.constraints.NotBlank

data class EmailRequest(
    @field:NotBlank
    val email: String,

    val sendType: SendType
) {
    init {
        Validator.email(email)
    }
}
