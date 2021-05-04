package sinhee.kang.tutorial.global.businessException.exception.post

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class ApplicationNotFoundException : BusinessException(ErrorCode.APPLICATION_NOT_FOUND) {
}