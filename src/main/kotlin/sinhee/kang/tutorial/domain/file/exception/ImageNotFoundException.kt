package sinhee.kang.tutorial.domain.file.exception

import sinhee.kang.tutorial.global.error.exception.BusinessException
import sinhee.kang.tutorial.global.error.exception.ErrorCode

class ImageNotFoundException: BusinessException(ErrorCode.IMAGE_NOT_FOUND) {
}