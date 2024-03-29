package sinhee.kang.tutorial.infra.util.exception.exceptions

import org.springframework.http.HttpStatus

abstract class BusinessException(val status: HttpStatus, override val message: String) : RuntimeException()
