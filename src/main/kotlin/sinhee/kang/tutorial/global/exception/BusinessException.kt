package sinhee.kang.tutorial.global.exception

import org.springframework.http.HttpStatus

open class BusinessException(var status: HttpStatus, override var message: String): RuntimeException()
