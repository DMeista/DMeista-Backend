package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.conflict

import org.springframework.http.HttpStatus.CONFLICT
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException

open class ConflictException : sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException {
    constructor() : super(CONFLICT, CONFLICT.reasonPhrase)
    constructor(message: String) : super(CONFLICT, message)
}
