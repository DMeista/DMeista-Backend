package sinhee.kang.tutorial.domain.auth.service.validate

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.service.email.SendType
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.businessException.exception.auth.UserAlreadyExistsException
import sinhee.kang.tutorial.global.businessException.exception.common.BadRequestException
import sinhee.kang.tutorial.global.businessException.exception.common.UserNotFoundException
import java.util.regex.Pattern

@Service
class ValidateServiceImpl(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository
): ValidateService {

    override fun validatePassword(password: String) {
        val emailRegex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}"

        if (!validateRegexPattern(emailRegex, password))
            throw BadRequestException()
    }

    override fun validateEmail(email: String) {
        val emailRegex = "^[0-9a-zA-Z._-]+@[0-9a-zA-Z.-]+\\.[a-zA-Z]{2,3}$"

        if (!validateRegexPattern(emailRegex, email))
            throw BadRequestException()
    }

    override fun validateExistEmail(email: String, sendType: SendType?) {
        val user = userRepository.findByEmail(email)

        when(sendType) {
            SendType.REGISTER -> user
                ?.let { throw UserAlreadyExistsException() }
            SendType.USER -> user
                ?: throw UserNotFoundException()
        }
    }

    override fun validateVerifiedEmail(email: String) {
        emailVerificationRepository.findById(email)
            .filter(EmailVerification::isVerify)
            .orElseThrow { BadRequestException() }
    }

    private fun validateRegexPattern(regex: String, input: String) =
        Pattern
            .compile(regex)
            .matcher(input)
            .matches()
}
