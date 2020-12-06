package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class TooManyEmailRequestException : BusinessException(ErrorCode.TOO_MANY_REQUEST)