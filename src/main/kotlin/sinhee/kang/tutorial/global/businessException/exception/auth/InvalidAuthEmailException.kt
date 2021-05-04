package sinhee.kang.tutorial.global.businessException.exception.auth

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class InvalidAuthEmailException : BusinessException(ErrorCode.INVALID_AUTH_EMAIL)