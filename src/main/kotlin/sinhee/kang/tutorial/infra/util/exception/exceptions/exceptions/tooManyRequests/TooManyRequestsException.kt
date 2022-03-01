package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.tooManyRequests

import org.springframework.http.HttpStatus.TOO_MANY_REQUESTS

open class TooManyRequestsException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(TOO_MANY_REQUESTS, TOO_MANY_REQUESTS.reasonPhrase)
    constructor(message: String) : super(TOO_MANY_REQUESTS, message)
}
