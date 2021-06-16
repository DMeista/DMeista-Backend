package sinhee.kang.tutorial.global.exception.exceptions.badRequest

import org.springframework.http.HttpStatus.BAD_REQUEST
import sinhee.kang.tutorial.global.exception.BusinessException

open class BadRequestException: BusinessException {
    constructor(): super(BAD_REQUEST, BAD_REQUEST.reasonPhrase)
    constructor(message: String): super(BAD_REQUEST, message)
}
