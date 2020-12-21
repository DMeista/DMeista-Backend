package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.user.UserService
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
        private var userService: UserService
) {
    @GetMapping
    fun hello(): String {
        return "hello"
    }

    @PostMapping("/email/verify/{sendType}")
    fun sendEmail(@RequestBody @Valid emailRequest: EmailRequest,
                  @PathVariable sendType: String): ResponseEntity<String> {
        userService.userAuthenticationSendEmail(sendType, emailRequest)
        return ResponseEntity.ok("ok")
    }

    @PutMapping("/email/verify")
    fun verifyEmail(@RequestBody verifyCodeRequest: VerifyCodeRequest) {
        userService.verifyEmail(verifyCodeRequest)
    }

    @GetMapping("/nickname")
    fun isVerifyNickname(@RequestParam nickname: String) {
        userService.isVerifyNickname(nickname)
    }

    @PostMapping
    fun signUp(@RequestBody signUpRequest: SignUpRequest) {
        userService.signUp(signUpRequest)
    }

    @DeleteMapping
    fun exitAccount(@RequestBody request: ChangePasswordRequest) {
        userService.exitAccount(request)
    }

    @PutMapping("/password")
    fun changePassword(@RequestBody changePasswordRequest: ChangePasswordRequest) {
        userService.changePassword(changePasswordRequest)
    }

    @PutMapping("/new-email")
    fun changeEmail(@RequestBody changeEmailRequest: ChangeEmailRequest) {
        userService.changeEmail(changeEmailRequest)
    }
}