package sinhee.kang.tutorial.application.dto.request.auth

import javax.validation.constraints.NotBlank

data class VerifyEmailRequest(
    @field:NotBlank
    val id: String,

    @field:NotBlank
    val authCode: String
)
