package sinhee.kang.tutorial.global.businessException.exception


open class BusinessException : RuntimeException {
    var errorCode: ErrorCode

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String?, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }
}