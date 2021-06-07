package sinhee.kang.tutorial.domain.auth.service.validate

import sinhee.kang.tutorial.domain.auth.service.email.SendType

interface ValidateService {
    fun validateEmail(email: String)

    fun validatePassword(password: String)

    fun validateNickname(nickname: String)

    fun validateExistEmail(email: String, sendType: SendType?)
}
