package sinhee.kang.tutorial.domain.auth.service.validate

import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType

interface ValidateService {
    fun validateEmail(email: String)

    fun validatePassword(password: String)

    fun checkExistNickname(nickname: String)

    fun checkExistEmail(email: String, sendType: SendType?)

    fun verifiedEmail(email: String)

    fun verifiedNickname(email: String, nickname: String)
}
