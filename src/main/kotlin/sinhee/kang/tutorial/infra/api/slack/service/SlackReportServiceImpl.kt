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
import sinhee.kang.tutorial.global.exception.ErrorRequestHandler
import sinhee.kang.tutorial.infra.api.slack.SlackApi
import sinhee.kang.tutorial.infra.api.slack.dto.Attachment
import sinhee.kang.tutorial.infra.api.slack.dto.Field
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest
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

    private fun attachDetailLog (request: HttpServletRequest, exception: Exception): SlackMessageRequest {
        val errorHandler = ErrorRequestHandler(request)

        val fieldList: MutableList<Field> = arrayListOf<Field>()
            .apply {
                add(Field(title = "Request Method", value = errorHandler.getMethod()))
                add(Field(title = "Request URI", value = errorHandler.getUri()))
                add(Field(title = "Request Header", value = errorHandler.getHeader()))
                add(Field(title = "Request Body", value = errorHandler.getBody()))
                add(Field(title = "Error StackTrace", value = errorHandler.getStackTrace(exception)))
            }
        val attachmentRequest: MutableList<Attachment> = arrayListOf<Attachment>()
            .apply {
                add(Attachment(
                    color = "#FF4444",
                    title = "[Server RuntimeError Report]",
                    footer = "Bug Reporter",
                    ts = System.currentTimeMillis(),
                    fields = fieldList
                ))
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
}
