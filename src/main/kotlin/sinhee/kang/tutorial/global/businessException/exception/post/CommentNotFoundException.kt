package sinhee.kang.tutorial.global.businessException.exception.post

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class CommentNotFoundException : BusinessException(ErrorCode.COMMENT_NOT_FOUND) {
}