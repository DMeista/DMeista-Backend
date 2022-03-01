package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized

import org.springframework.http.HttpStatus.UNAUTHORIZED

open class UnAuthorizedException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(UNAUTHORIZED, UNAUTHORIZED.reasonPhrase)
    constructor(message: String) : super(UNAUTHORIZED, message)
}
