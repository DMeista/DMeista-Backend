package sinhee.kang.tutorial.controller.auth

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.application.dto.request.auth.*
import sinhee.kang.tutorial.application.service.auth.AuthService
import sinhee.kang.tutorial.application.service.email.EmailService
import sinhee.kang.tutorial.application.service.email.enums.SendType
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication
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

    @Authentication
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
