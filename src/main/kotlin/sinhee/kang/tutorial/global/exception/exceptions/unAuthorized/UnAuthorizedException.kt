package sinhee.kang.tutorial.global.exception.exceptions.unAuthorized

import org.springframework.http.HttpStatus.UNAUTHORIZED
import sinhee.kang.tutorial.global.exception.BusinessException

open class UnAuthorizedException : BusinessException {
    constructor() : super(UNAUTHORIZED, UNAUTHORIZED.reasonPhrase)
    constructor(message: String) : super(UNAUTHORIZED, message)
}
