package sinhee.kang.tutorial.domain.auth.service.validate

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.entity.verification.AuthVerification
import sinhee.kang.tutorial.domain.auth.repository.verification.AuthVerificationRepository

import sinhee.kang.tutorial.domain.auth.service.email.enums.SendType
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.conflict.UserAlreadyExistsException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.InvalidAuthEmailException
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

@Service
class ValidateServiceImpl(
    private val userRepository: UserRepository,
    private val authVerificationRepository: AuthVerificationRepository
): ValidateService {

    override fun validateEmail(email: String) {
        validateRegexType(email, ValidateType.EMAIL)
            throw BadRequestException()
    }

    override fun validatePassword(password: String) {
        validateRegexType(password, ValidateType.PASSWORD)
            throw BadRequestException()
    }

    override fun checkExistNickname(nickname: String) {
        userRepository.findByNickname(nickname)
            ?.let { throw UserAlreadyExistsException() }

        if (nickname.isEmpty() || nickname.isBlank())
            throw BadRequestException()
    }

    override fun checkExistEmail(email: String, sendType: SendType?) {
        val user = userRepository.findByEmail(email)

        when(sendType) {
            SendType.REGISTER -> user
                ?.let { throw UserAlreadyExistsException() }
            SendType.USER -> user
                ?: throw UserNotFoundException()
        }
    }

    override fun verifiedEmail(email: String) {
        authVerificationRepository.findById(email)
            .filter(AuthVerification::isVerify)
            .orElseThrow { InvalidAuthEmailException() }
    }

    override fun verifiedNickname(email: String, nickname: String) {
        authVerificationRepository.findById(email)
            .orElseThrow { BadRequestException() }
            .checkEqualNickname(nickname)
    }

    private fun validateRegexType(string: String, vararg types: ValidateType) {
        types.forEach { regexType ->
            regexType.isValid(string)
        }
    }

    private fun ValidateType.isValid(input: String): Boolean =
        regex.matches(input)
}
