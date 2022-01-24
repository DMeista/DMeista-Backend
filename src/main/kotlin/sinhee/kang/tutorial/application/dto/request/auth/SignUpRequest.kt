package sinhee.kang.tutorial.application.dto.request.auth

import sinhee.kang.tutorial.domain.user.entity.user.User
import javax.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val nickname: String
) {
    init {
        with(sinhee.kang.tutorial.infra.util.validator.Validator) {
            email(email)
            password(password)
        }
    }

    fun toEntity(): User =
        User(
            email = email,
            nickname = nickname
        )
}
