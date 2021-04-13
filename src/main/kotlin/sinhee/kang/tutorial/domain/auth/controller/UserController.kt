package sinhee.kang.tutorial.domain.auth.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.auth.service.user.UserService
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService
) {

    @PostMapping("/email/verify/{sendType}")
    fun sendEmail(@Valid @RequestBody emailRequest: EmailRequest,
                  @PathVariable sendType: String): HttpStatus =
        userService.userAuthenticationSendEmail(sendType, emailRequest)

    @PutMapping("/email/verify")
    fun verifyEmail(@Valid @RequestBody verifyCodeRequest: VerifyCodeRequest): HttpStatus =
        userService.verifyEmail(verifyCodeRequest)

    @GetMapping("/nickname")
    fun isVerifyNickname(@RequestParam nickname: String): HttpStatus =
        userService.isVerifyNickname(nickname)

    @PostMapping
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest) =
        userService.signUp(signUpRequest)

    @DeleteMapping
    fun exitAccount(@Valid @RequestBody request: ChangePasswordRequest) =
        userService.exitAccount(request)

    @PutMapping("/password")
    fun changePassword(@Valid @RequestBody changePasswordRequest: ChangePasswordRequest) =
        userService.changePassword(changePasswordRequest)

}
