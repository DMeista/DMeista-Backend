package sinhee.kang.tutorial.domain.post.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class ApplicationNotFoundException : BusinessException(ErrorCode.APPLICATION_NOT_FOUND) {
}