package sinhee.kang.tutorial.domain.post.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class PermissionDeniedException : BusinessException(ErrorCode.PERMISSION_DENIED_EXCEPTION) {
}