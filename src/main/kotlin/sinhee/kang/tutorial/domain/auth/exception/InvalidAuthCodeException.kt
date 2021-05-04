package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class InvalidAuthCodeException : BusinessException(ErrorCode.INVALID_AUTH_CODE)