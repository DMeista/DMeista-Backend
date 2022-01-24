package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized

import org.springframework.http.HttpStatus.UNAUTHORIZED
import sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException

open class UnAuthorizedException : sinhee.kang.tutorial.infra.util.exception.exceptions.BusinessException {
    constructor() : super(UNAUTHORIZED, UNAUTHORIZED.reasonPhrase)
    constructor(message: String) : super(UNAUTHORIZED, message)
}
