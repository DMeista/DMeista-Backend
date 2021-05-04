package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class TooManyEmailRequestException : BusinessException(ErrorCode.TOO_MANY_REQUEST)