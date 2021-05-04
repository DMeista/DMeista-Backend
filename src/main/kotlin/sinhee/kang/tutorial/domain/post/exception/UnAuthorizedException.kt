package sinhee.kang.tutorial.domain.post.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class UnAuthorizedException : BusinessException(ErrorCode.UNAUTHORIZED) {
}