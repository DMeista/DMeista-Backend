package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.user.UserService
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class AuthController(
        private var userService: UserService
) {

    @PostMapping("/email/verify/{sendType}")
    fun sendEmail(@RequestBody @Valid emailRequest: EmailRequest,
                  @PathVariable sendType: String) {
        userService.userAuthenticationSendEmail(sendType, emailRequest)
    }

    @PutMapping("/email/verify")
    fun verifyEmail(@RequestBody @Valid verifyCodeRequest: VerifyCodeRequest) {
        userService.verifyEmail(verifyCodeRequest)
    }

    @PostMapping
    fun signUp(@RequestBody @Valid signUpRequest: SignUpRequest) {
        userService.signUp(signUpRequest)
    }

    @DeleteMapping
    fun exitAccount(@RequestBody @Valid request: ChangePasswordRequest) {
        userService.exitAccount(request)
    }

    @PutMapping("/password")
    fun changePassword(@RequestBody @Valid changePasswordRequest: ChangePasswordRequest) {
        userService.changePassword(changePasswordRequest)
    }

    @PutMapping("/new-email")
    fun changeEmail(@RequestBody @Valid changeEmailRequest: ChangeEmailRequest) {
        userService.changeEmail(changeEmailRequest)
    }
}