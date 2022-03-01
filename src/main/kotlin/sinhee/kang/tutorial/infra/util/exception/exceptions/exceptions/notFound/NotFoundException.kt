package sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound

import org.springframework.http.HttpStatus.NOT_FOUND

open class NotFoundException : sinhee.kang.tutorial.infra.util.exception.exceptions.ApiException {
    constructor() : super(NOT_FOUND, NOT_FOUND.reasonPhrase)
    constructor(message: String) : super(NOT_FOUND, message)
}
