package sinhee.kang.tutorial.global.config.security.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class BadRequestException : BusinessException(ErrorCode.BAD_REQUEST)