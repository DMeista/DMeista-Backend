package sinhee.kang.tutorial.global.exception.exceptions.conflict

import org.springframework.http.HttpStatus.CONFLICT
import sinhee.kang.tutorial.global.exception.BusinessException

open class ConflictException : BusinessException {
    constructor() : super(CONFLICT, CONFLICT.reasonPhrase)
    constructor(message: String) : super(CONFLICT, message)
}
