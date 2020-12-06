package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class ExpiredTokenException : BusinessException(ErrorCode.EXPIRED_TOKEN)