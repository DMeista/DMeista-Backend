package sinhee.kang.tutorial.global.businessException.exception.common

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class BadRequestException : BusinessException(ErrorCode.BAD_REQUEST)