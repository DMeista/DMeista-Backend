package sinhee.kang.tutorial.global.error

import org.springframework.stereotype.Component
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Component
class ErrorHandler {
    fun getStackTrace(e: Exception): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        return stringWriter.toString()
    }

    fun getHeader(request: HttpServletRequest): String {
        val headers = Collections.list(request.headerNames)
                .stream()
                .collect(Collectors.toMap({ h: String? -> h }) { name: String? -> request.getHeader(name) })
        return headers.entries.toTypedArray().contentToString()
    }

    fun getBody(request: HttpServletRequest): String {
        try {
            return request.reader.lines().collect(Collectors.joining())
        } catch (e: IOException) {
            e.message
        }
        return ""
    }
}