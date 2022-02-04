package sinhee.kang.tutorial.infra.util.validator

import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException
import sinhee.kang.tutorial.infra.util.validator.enums.ValidateType

object Validator {
    fun email(value: String) {
        if (ValidateType.EMAIL.isValid(value))
            throw InvalidArgumentException()
    }

    fun password(value: String) {
        if (ValidateType.PASSWORD.isValid(value))
            throw InvalidArgumentException()
    }

    private fun ValidateType.isValid(input: String): Boolean =
        !regex.matches(input)
}
