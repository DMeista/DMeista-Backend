package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.forbidden

import org.springframework.http.HttpStatus.FORBIDDEN

open class ForbiddenException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(FORBIDDEN, FORBIDDEN.reasonPhrase)
    constructor(message: String) : super(FORBIDDEN, message)
}
