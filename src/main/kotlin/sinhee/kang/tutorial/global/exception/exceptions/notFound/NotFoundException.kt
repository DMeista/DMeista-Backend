package sinhee.kang.tutorial.global.exception.exceptions.notFound

import org.springframework.http.HttpStatus.NOT_FOUND
import sinhee.kang.tutorial.global.exception.BusinessException

open class NotFoundException: BusinessException {
    constructor(): super(NOT_FOUND, NOT_FOUND.reasonPhrase)
    constructor(message: String): super(NOT_FOUND, message)
}