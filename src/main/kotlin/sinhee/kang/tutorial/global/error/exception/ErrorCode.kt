package sinhee.kang.tutorial.global.error.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(var status: Int, var message: String) {
    //Common
    BAD_REQUEST(400, "Bad Request(Invalid Parameter)"),
    USER_NOT_FOUND(404, "User Not Found."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    //Auth
    INVALID_AUTH_EMAIL(400, "Invalid Auth Email"),
    INVALID_AUTH_CODE(400, "Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400, "Expired Auth Code"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    UNAUTHORIZED(401, "Authentication is required and has failed or has not yet been provided"),
    USER_DUPLICATION(409, "User is Already Exists"),
    TOO_MANY_REQUEST(429, "Too Many Request"),

    //Post
    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    APPLICATION_NOT_FOUND(404, "Application Not Found"),
    PERMISSION_DENIED_EXCEPTION(401, "Permission Denied")
}