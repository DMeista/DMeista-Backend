package sinhee.kang.tutorial.domain.auth.service.email

interface EmailService {
    fun sendEmail(email: String, code: String)
}