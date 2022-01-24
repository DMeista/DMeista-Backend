package sinhee.kang.tutorial.infra.util.validator

import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException
import sinhee.kang.tutorial.infra.util.validator.enums.ValidateType

object Validator {
    fun email(value: String) {
        if (sinhee.kang.tutorial.infra.util.validator.enums.ValidateType.EMAIL.isValid(value))
            throw sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException()
    }

    fun password(value: String) {
        if (sinhee.kang.tutorial.infra.util.validator.enums.ValidateType.PASSWORD.isValid(value))
            throw sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.InvalidArgumentException()
    }

    private fun sinhee.kang.tutorial.infra.util.validator.enums.ValidateType.isValid(input: String): Boolean =
        !regex.matches(input)
}
