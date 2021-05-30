package sinhee.kang.tutorial.domain.auth.service.validate

import sinhee.kang.tutorial.domain.auth.service.email.SendType

interface ValidateService {
    fun validatePassword(password: String)

    fun validateEmail(email: String)

    fun validateExistEmail(email: String, sendType: SendType?)

    fun validateVerifiedEmail(email: String)
}
