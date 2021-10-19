package sinhee.kang.tutorial.domain.auth.dto.request

import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.global.util.validator.Validator
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
        with(Validator) {
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
