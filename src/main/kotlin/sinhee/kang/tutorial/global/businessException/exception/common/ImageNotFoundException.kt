package sinhee.kang.tutorial.global.businessException.exception.common

import sinhee.kang.tutorial.global.businessException.BusinessException
import sinhee.kang.tutorial.global.businessException.ErrorCode

class ImageNotFoundException: BusinessException(ErrorCode.IMAGE_NOT_FOUND) {
}