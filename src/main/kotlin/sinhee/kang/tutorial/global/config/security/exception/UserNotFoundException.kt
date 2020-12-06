package sinhee.kang.tutorial.global.config.security.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class UserNotFoundException : BusinessException(ErrorCode.USER_NOT_FOUND)