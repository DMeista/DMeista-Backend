package sinhee.kang.tutorial.global.businessException.exception.common

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class UserNotFoundException : BusinessException(ErrorCode.USER_NOT_FOUND)