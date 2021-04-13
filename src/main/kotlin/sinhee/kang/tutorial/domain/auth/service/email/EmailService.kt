package sinhee.kang.tutorial.domain.auth.service.email

import sinhee.kang.tutorial.domain.user.domain.user.User

interface EmailService {
    fun sendVerifyEmail(email: String, code: String)
    fun sendCelebrateEmail(user: User)
}
