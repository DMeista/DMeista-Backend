package sinhee.kang.tutorial.global.exception.exceptions.notFound

import org.springframework.http.HttpStatus.NOT_FOUND
import sinhee.kang.tutorial.global.exception.BusinessException

open class NotFoundException: BusinessException {
    constructor(): super(NOT_FOUND)
    constructor(message: String): super(NOT_FOUND, message)
}