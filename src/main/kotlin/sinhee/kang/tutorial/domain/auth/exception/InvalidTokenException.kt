package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class InvalidTokenException : BusinessException(ErrorCode.INVALID_TOKEN)