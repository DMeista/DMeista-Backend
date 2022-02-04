package sinhee.kang.tutorial.infra.util.requestLog

import org.apache.commons.io.IOUtils
import java.io.*
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class MultiReadableHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    override fun getInputStream(): ServletInputStream {
        val byteArray: ByteArray = IOUtils.toByteArray(request.inputStream)

        return CachedServletInputStream(byteArray)
    }

    override fun getReader(): BufferedReader =
        BufferedReader(InputStreamReader(inputStream))

    inner class CachedServletInputStream(content: ByteArray): ServletInputStream() {
        private val buffer = ByteArrayInputStream(content)

        override fun read(): Int =
            buffer.read()

        override fun isFinished(): Boolean =
            buffer.available() == 0

        override fun isReady(): Boolean =
            true

        override fun setReadListener(listener: ReadListener) {}
    }
}
