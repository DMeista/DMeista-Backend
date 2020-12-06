package sinhee.kang.tutorial.domain.post.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class CommentNotFoundException : BusinessException(ErrorCode.COMMENT_NOT_FOUND) {
}