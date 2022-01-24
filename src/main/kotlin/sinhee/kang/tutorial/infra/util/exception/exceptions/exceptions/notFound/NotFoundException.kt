package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound

import org.springframework.http.HttpStatus.NOT_FOUND
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException

open class NotFoundException : sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException {
    constructor() : super(NOT_FOUND, NOT_FOUND.reasonPhrase)
    constructor(message: String) : super(NOT_FOUND, message)
}
