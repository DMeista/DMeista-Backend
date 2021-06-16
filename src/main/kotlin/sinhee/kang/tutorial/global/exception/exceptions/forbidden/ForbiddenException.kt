package sinhee.kang.tutorial.global.exception.exceptions.forbidden

import org.springframework.http.HttpStatus.FORBIDDEN
import sinhee.kang.tutorial.global.exception.BusinessException

open class ForbiddenException: BusinessException {
    constructor(): super(FORBIDDEN)
    constructor(message: String): super(FORBIDDEN, message)
}
