package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest

import org.springframework.http.HttpStatus.BAD_REQUEST
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException

open class BadRequestException : sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException {
    constructor() : super(BAD_REQUEST, BAD_REQUEST.reasonPhrase)
    constructor(message: String) : super(BAD_REQUEST, message)
}
