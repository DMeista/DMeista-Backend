package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.conflict

import org.springframework.http.HttpStatus.CONFLICT

open class ConflictException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(CONFLICT, CONFLICT.reasonPhrase)
    constructor(message: String) : super(CONFLICT, message)
}
