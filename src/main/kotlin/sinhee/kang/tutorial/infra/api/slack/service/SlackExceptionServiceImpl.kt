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
import sinhee.kang.tutorial.global.businessException.ErrorHandler
import sinhee.kang.tutorial.infra.api.slack.SlackApi
import sinhee.kang.tutorial.infra.api.slack.dto.Attachment
import sinhee.kang.tutorial.infra.api.slack.dto.Field
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest
import javax.servlet.http.HttpServletRequest

@Component
class SlackExceptionServiceImpl: SlackExceptionService {
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
        val errorHandler = ErrorHandler()

        val attachmentRequest: MutableList<Attachment> = ArrayList()
        val fieldList: MutableList<Field> = ArrayList()

        fieldList.apply {
            add(Field(title = "Request URI", value = request.requestURI))
            add(Field(title = "Request Header", value = errorHandler.getHeader(request)))
            add(Field(title = "Request Body", value = errorHandler.getBody(request)))
        }

        attachmentRequest.add(Attachment(
            color = "#FF4444",
            pretext = "[Server RuntimeError Report]",
            tile = exception.localizedMessage,
            text = errorHandler.getStackTrace(exception),
            footer = "Bug Reporter",
            ts = System.currentTimeMillis() / 1000,
            fields = fieldList
        ))

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
