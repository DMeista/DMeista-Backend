package sinhee.kang.tutorial.global.businessException

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(var status: Int, var message: String) {
    BAD_REQUEST(400, "Bad Request(Invalid Parameter)"),
    ACCESS_DENIED(400, "Access Denied"),
    INCORRECT_PASSWORD(403, "Incorrect Password"),
    USER_NOT_FOUND(404, "User Not Found."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    INVALID_AUTH_EMAIL(400, "Invalid Auth Email"),
    INVALID_AUTH_CODE(400, "Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400, "Expired Auth Code"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    UNAUTHORIZED(401, "Authentication is required and has failed or has not yet been provided"),
    USER_DUPLICATION(409, "User is Already Exists"),
    TOO_MANY_REQUEST(429, "Too Many Request"),
    PERMISSION_DENIED(401, "Permission Denied"),

    APPLICATION_NOT_FOUND(404, "Application Not Found"),
    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    IMAGE_NOT_FOUND(404, "Image Not Found")
}
