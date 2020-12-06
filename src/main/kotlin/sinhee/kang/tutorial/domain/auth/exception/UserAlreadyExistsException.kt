package sinhee.kang.tutorial.domain.auth.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class UserAlreadyExistsException : BusinessException(ErrorCode.USER_DUPLICATION)