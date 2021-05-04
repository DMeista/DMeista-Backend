package sinhee.kang.tutorial.domain.file.exception

import sinhee.kang.tutorial.global.businessException.exception.BusinessException
import sinhee.kang.tutorial.global.businessException.exception.ErrorCode

class ImageNotFoundException: BusinessException(ErrorCode.IMAGE_NOT_FOUND) {
}