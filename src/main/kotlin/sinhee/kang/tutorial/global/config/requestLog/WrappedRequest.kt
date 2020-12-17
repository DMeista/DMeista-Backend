package sinhee.kang.tutorial.global.config.requestLog

import io.micrometer.core.instrument.util.StringUtils
import org.apache.commons.io.IOUtils
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import kotlin.collections.HashMap

class WrappedRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var encoding: Charset? = null
    private var rawData: ByteArray? = null
    private val params: MutableMap<String, Array<String>> = HashMap()

    init {
        params.putAll(request.parameterMap)
        val charEncoding = request.characterEncoding
        encoding = if (StringUtils.isBlank(charEncoding)) StandardCharsets.UTF_8 else Charset.forName(charEncoding)
        try {
            val `is`: InputStream = request.inputStream
            rawData = IOUtils.toByteArray(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getParameter(name: String?): String? {
        val paramArray = getParameterValues(name)
        return if (paramArray != null && paramArray.isNotEmpty()) {
            paramArray[0]
        } else {
            null
        }
    }

    override fun getParameterMap(): Map<String?, Array<String>> {
        return Collections.unmodifiableMap<String, Array<String>>(params)
    }

    override fun getParameterNames(): Enumeration<String> {
        return Collections.enumeration(params.keys as Collection<String>)
    }

    override fun getParameterValues(name: String?): Array<String?>? {
        var result: Array<String?>? = null
        val dummyParamValue = params[name]
        if (dummyParamValue != null) {
            result = arrayOfNulls(dummyParamValue.size)
            System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.size)
        }
        return result
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(rawData)
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(listener: ReadListener) {}
            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream))
    }
}