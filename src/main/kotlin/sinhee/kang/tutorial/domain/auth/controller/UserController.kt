package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.email.EmailService
import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import sinhee.kang.tutorial.domain.auth.service.user.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService
) {

    @PostMapping("/email/verify/{sendType}")
    fun sendEmail(
        @RequestBody emailRequest: EmailRequest,
        @PathVariable sendType: SendType
    ) = emailService.sendVerifyEmail(emailRequest, sendType)

    @PutMapping("/email/verify")
    fun verifyEmail(@RequestBody verifyCodeRequest: VerifyCodeRequest) =
        emailService.verifyAuthCode(verifyCodeRequest)

    @GetMapping("/nickname")
    fun verifyNickname(@RequestBody verifyNicknameRequest: VerifyNicknameRequest) =
        userService.isVerifyNickname(verifyNicknameRequest)

    @PutMapping("/password")
    fun changePassword(@RequestBody changePasswordRequest: ChangePasswordRequest) =
        userService.changePassword(changePasswordRequest)

    @DeleteMapping
    fun exitAccount(@RequestBody request: ChangePasswordRequest) =
        userService.exitAccount(request)
}
