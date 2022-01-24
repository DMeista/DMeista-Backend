package sinhee.kang.tutorial.application.dto.request.auth

import sinhee.kang.tutorial.application.service.email.enums.SendType
import sinhee.kang.tutorial.infra.util.validator.Validator
import javax.validation.constraints.NotBlank

data class EmailRequest(
    @field:NotBlank
    val email: String,

    val sendType: SendType
) {
    init {
        sinhee.kang.tutorial.infra.util.validator.Validator.email(email)
    }
}
