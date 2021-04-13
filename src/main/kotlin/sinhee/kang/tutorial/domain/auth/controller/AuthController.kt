package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
        private var authService: AuthService
) {
    @PostMapping
    fun signIn(@Valid @RequestBody dto: SignInRequest): TokenResponse {
        return authService.signIn(dto)
    }

    @PutMapping
    fun refreshToken(@RequestHeader("X-Refresh-Token") refreshToken: String): TokenResponse {
        return authService.refreshToken(refreshToken)
    }
}
