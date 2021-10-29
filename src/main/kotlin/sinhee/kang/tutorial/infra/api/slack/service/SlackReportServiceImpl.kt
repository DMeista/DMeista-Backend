package sinhee.kang.tutorial.infra.api.slack.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import sinhee.kang.tutorial.infra.api.slack.SlackApi
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest.Attachment
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest.Field
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Component
class SlackReportServiceImpl : SlackReportService {

    private val connection = Retrofit.Builder()
        .baseUrl("https://hooks.slack.com/services/")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(SlackApi::class.java)

    override fun sendMessage(request: HttpServletRequest, exception: Exception) {
        val message: SlackMessageRequest = attachRequestLog(request, exception)
        val requestBody: RequestBody = serializedRequest(message)

        connection.sendMessage(requestBody).execute()
    }

    private fun attachRequestLog(request: HttpServletRequest, exception: Exception): SlackMessageRequest {
        val fieldContent: List<Field> = arrayListOf(
            Field("Request Method", "[${request.method}]"),
            Field("Request URI", request.requestURI),
            Field("Request Header", request.getHeaders()),
            Field("Request Body", request.getBody()),
            Field("Error StackTrace", exception.stackTrace.contentToString())
        )
        val attachmentMessage: List<Attachment> = arrayListOf(
            Attachment(fields = fieldContent)
        )

        return SlackMessageRequest(attachmentMessage)
    }

    private fun serializedRequest(request: SlackMessageRequest): RequestBody =
        try {
            RequestBody.create(
                MultipartBody.FORM,
                ObjectMapper().writeValueAsString(request)
            )
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e.message)
        }

    private fun HttpServletRequest.getHeaders(): String {
        val headers = headerNames.toList()
            .associateWith { getHeader(it) }
            .entries

        return headers.toString()
    }

    private fun HttpServletRequest.getBody(): String =
        try {
            reader.lines().collect(Collectors.joining())
        } catch (e: IOException) {
            e.message.toString()
        }
}
