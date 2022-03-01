package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest

import org.springframework.http.HttpStatus.BAD_REQUEST

open class BadRequestException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(BAD_REQUEST, BAD_REQUEST.reasonPhrase)
    constructor(message: String) : super(BAD_REQUEST, message)
}
