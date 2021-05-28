package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.SendType
import sinhee.kang.tutorial.domain.auth.service.user.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService
) {

    @PostMapping("/email/verify/{sendType}")
    fun sendEmail(@RequestBody emailRequest: EmailRequest,
                  @PathVariable sendType: SendType) =
        emailService.sendVerificationEmail(emailRequest, sendType)

    @PutMapping("/email/verify")
    fun verifyEmail(@RequestBody verifyCodeRequest: VerifyCodeRequest) =
        emailService.verifyEmail(verifyCodeRequest)

    @GetMapping("/nickname")
    fun isVerifyNickname(@RequestParam nickname: String) =
        userService.isVerifyNickname(nickname)

    @PostMapping
    fun signUp(@RequestBody signUpRequest: SignUpRequest) =
        userService.signUp(signUpRequest)

    @DeleteMapping
    fun exitAccount(@RequestBody request: ChangePasswordRequest) =
        userService.exitAccount(request)

    @PutMapping("/password")
    fun changePassword(@RequestBody changePasswordRequest: ChangePasswordRequest) =
        userService.changePassword(changePasswordRequest)
}
