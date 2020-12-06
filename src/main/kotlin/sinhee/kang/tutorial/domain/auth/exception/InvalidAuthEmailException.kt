package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class InvalidAuthEmailException : BusinessException(ErrorCode.INVALID_AUTH_EMAIL)