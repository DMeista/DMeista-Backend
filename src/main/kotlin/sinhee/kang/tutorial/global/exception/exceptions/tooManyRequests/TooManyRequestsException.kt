package sinhee.kang.tutorial.global.exception.exceptions.tooManyRequests

import org.springframework.http.HttpStatus.TOO_MANY_REQUESTS
import sinhee.kang.tutorial.global.exception.BusinessException

open class TooManyRequestsException: BusinessException {
    constructor(): super(TOO_MANY_REQUESTS, TOO_MANY_REQUESTS.reasonPhrase)
    constructor(message: String): super(TOO_MANY_REQUESTS, message)
}
