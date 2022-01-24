package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.forbidden

import org.springframework.http.HttpStatus.FORBIDDEN
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException

open class ForbiddenException : sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException {
    constructor() : super(FORBIDDEN, FORBIDDEN.reasonPhrase)
    constructor(message: String) : super(FORBIDDEN, message)
}
