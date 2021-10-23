package sinhee.kang.tutorial.domain.auth.dto.request

import javax.validation.constraints.NotBlank

data class VerifyEmailRequest(
    @field:NotBlank
    val id: String,

    @field:NotBlank
    val authCode: String
)
