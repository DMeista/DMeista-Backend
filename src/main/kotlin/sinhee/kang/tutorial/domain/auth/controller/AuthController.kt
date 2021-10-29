package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val emailService: EmailService
) {

    @PostMapping("/login")
    fun signIn(@RequestBody signInRequest: SignInRequest, response: HttpServletResponse) =
        authService.signIn(signInRequest, response)

    @PostMapping("/register")
    fun signUp(@RequestBody signUpRequest: SignUpRequest) =
        authService.signUp(signUpRequest)

    @PutMapping("/password")
    fun changePassword(@RequestBody changePasswordRequest: ChangePasswordRequest) =
        authService.changePassword(changePasswordRequest)

    @DeleteMapping
    fun exitService(@RequestBody exitServiceRequest: SignInRequest) =
        authService.exitAccount(exitServiceRequest)

    @GetMapping("/verify/nickname")
    fun verifyNickname(@RequestParam nickname: String) =
        authService.verifyNickname(nickname)

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam id: String,
        @RequestParam(name = "auth_code") authCode: String
    ) = authService.verifyEmail(VerifyEmailRequest(id, authCode))

    @PostMapping("/verify/email/{sendType}")
    fun sendEmail(
        @RequestParam email: String,
        @PathVariable sendType: SendType
    ) = emailService.sendAuthCode(EmailRequest(email, sendType))

    @PutMapping
    fun extendTokens(request: HttpServletRequest, response: HttpServletResponse) =
        authService.extendToken(request, response)
}
