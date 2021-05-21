package sinhee.kang.tutorial.domain.auth.dto.request

import org.springframework.security.crypto.password.PasswordEncoder
import sinhee.kang.tutorial.domain.user.domain.user.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SignUpRequest(
        @NotBlank
        @Email
        val email: String,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_~])[A-Za-z\\d@$!%*?&_~]{8,}$")
        val password: String,

        @NotBlank
        val nickname: String
) {
        fun toEntity(passwordEncoder: PasswordEncoder): User =
            User(
                email = email,
                password = passwordEncoder.encode(password),
                nickname = nickname
            )
}
