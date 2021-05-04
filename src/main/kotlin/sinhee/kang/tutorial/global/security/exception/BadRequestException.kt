package sinhee.kang.tutorial.global.security.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class BadRequestException : BusinessException(ErrorCode.BAD_REQUEST)