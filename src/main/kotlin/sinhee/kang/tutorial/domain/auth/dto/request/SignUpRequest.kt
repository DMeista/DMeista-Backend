package sinhee.kang.tutorial.domain.auth.dto.request

import org.springframework.security.crypto.password.PasswordEncoder
import sinhee.kang.tutorial.domain.user.domain.user.User

data class SignUpRequest(
    val email: String,

    val password: String,

    val nickname: String
) {
    fun toEntity(passwordEncoder: PasswordEncoder): User =
        User(
            email = email,
            password = passwordEncoder.encode(password),
            nickname = nickname
        )
}
