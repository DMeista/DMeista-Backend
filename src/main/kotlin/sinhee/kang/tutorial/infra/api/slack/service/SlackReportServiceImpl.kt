package sinhee.kang.tutorial.infra.api.slack.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import sinhee.kang.tutorial.infra.api.slack.SlackApi
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest.Field
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest.Attachment
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Component
class SlackReportServiceImpl: SlackReportService {
    private val connection = Retrofit
        .Builder()
            .baseUrl("https://hooks.slack.com/services/")
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
        .create(SlackApi::class.java)

    override fun sendMessage(request: HttpServletRequest, exception: Exception) {
        val body: RequestBody = serializedRequest(attachDetailLog(request, exception))
        connection.sendMessage(body).execute()
    }

    private fun attachDetailLog(request: HttpServletRequest, exception: Exception): SlackMessageRequest {

        val fieldList: MutableList<Field> = arrayListOf<Field>()
            .apply {
                add(Field(title = "Request Method", value = request.getMethod()))
                add(Field(title = "Request URI", value = request.getRequestURI()))
                add(Field(title = "Request Header", value = request.getHeader()))
                add(Field(title = "Request Body", value = request.getBody()))
                add(Field(title = "Error StackTrace", value = exception.stackTrace()))
            }
        val attachmentRequest: MutableList<Attachment> = arrayListOf<Attachment>()
            .apply {
                add(
                    Attachment(
                    color = "#FF4444",
                    title = "[Server RuntimeError Report]",
                    footer = "Bug Reporter",
                    ts = System.currentTimeMillis(),
                    fields = fieldList
                )
                )
            }

        return SlackMessageRequest(attachmentRequest)
    }

    private fun serializedRequest(obj: SlackMessageRequest): RequestBody {
        val mapper = ObjectMapper()
        return try {
            mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            RequestBody.create(MultipartBody.FORM, mapper.writeValueAsString(obj))
        }
        catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e.message)
        }
    }

    private fun HttpServletRequest.getHeader(): String {
        val headers = Collections
            .list(headerNames).stream()
            .collect(Collectors.toMap({ h: String? -> h }) { name: String? -> getHeader(name) })
        return headers.entries.toTypedArray().contentToString()
    }

    private fun HttpServletRequest.getBody(): String {
        return try {
            reader.lines().collect(Collectors.joining())
        } catch (e: IOException) {
            e.message.toString()
        }
    }

    private fun Exception.stackTrace(): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        printStackTrace(printWriter)
        return stringWriter.toString()
    }
}
