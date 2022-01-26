package sinhee.kang.tutorial.infra.util.requestLog

import org.apache.commons.io.IOUtils
import java.io.*
import java.util.*
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import kotlin.collections.HashMap

class WrappedRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var rawData: ByteArray = byteArrayOf()
    private val params: MutableMap<String, Array<String>> = HashMap()

    init {
        params.putAll(request.parameterMap)

        try {
            rawData = IOUtils.toByteArray(request.inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getParameter(name: String?): String? {
        val paramArray = getParameterValues(name)
        return if (!paramArray.isNullOrEmpty()) {
            paramArray[0]
        } else null
    }

    override fun getParameterMap(): Map<String?, Array<String>> =
        Collections.unmodifiableMap<String, Array<String>>(params)

    override fun getParameterNames(): Enumeration<String> =
        Collections.enumeration(params.keys as Collection<String>)

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
            override fun isFinished(): Boolean = false

            override fun isReady(): Boolean = false

            override fun setReadListener(listener: ReadListener) {}

            override fun read(): Int = byteArrayInputStream.read()
        }
    }

    override fun getReader(): BufferedReader =
        BufferedReader(InputStreamReader(this.inputStream))
}
