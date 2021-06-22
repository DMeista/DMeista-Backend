package sinhee.kang.tutorial.domain.auth.service.validate

enum class ValidateType(var regex: Regex) {
    EMAIL("^[0-9a-zA-Z._-]+@[0-9a-zA-Z.-]+\\.[a-zA-Z]{2,3}\$".toRegex()),
    PASSWORD("(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}".toRegex())
}