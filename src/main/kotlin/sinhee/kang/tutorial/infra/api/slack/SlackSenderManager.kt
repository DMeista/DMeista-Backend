package sinhee.kang.tutorial.infra.api.slack

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import okhttp3.*
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import sinhee.kang.tutorial.global.error.ErrorHandler
import sinhee.kang.tutorial.infra.api.slack.dto.Attachment
import sinhee.kang.tutorial.infra.api.slack.dto.Field
import sinhee.kang.tutorial.infra.api.slack.dto.SlackMessageRequest
import javax.servlet.http.HttpServletRequest

@Component
class SlackSenderManager {
    private var errorHandler = ErrorHandler()

    private var webHookUrl: String = System.getenv("WEB_HOOK_URL")
    private var client = OkHttpClient()
    private var mapper = ObjectMapper()

    fun send(exception: Exception) {
        val request: HttpServletRequest = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        this.send(request, exception)
    }

    fun send(request: HttpServletRequest, exception: Exception) {
        val attachment: SlackMessageRequest = this.toAttachments(request, exception)
        val sendRequest: Request = Request.Builder()
                .url(webHookUrl)
                .addHeader("Content-Type", "application/json")
                .post(writeValueAsRequestBody(attachment))
                .build()
        client.newCall(sendRequest).execute().body()!!.string()
    }

    fun toAttachments(request: HttpServletRequest, exception: Exception): SlackMessageRequest {
        val attachmentRequest: MutableList<Attachment> = ArrayList()
        val fieldList: MutableList<Field> = ArrayList()
        fieldList.add(Field(title = "Request URI", value = request.requestURI))
        fieldList.add(Field(title = "Request Header", value = errorHandler.getHeader(request)))
        fieldList.add(Field(title = "Request Body", value = errorHandler.getBody(request)))

        attachmentRequest.add(Attachment(
                color = "#FF4444",
                pretext = "[Server RuntimeError Report]",
                tile = exception.localizedMessage,
                text = errorHandler.getStackTrace(exception),
                footer = "Bug Reporter",
                ts = System.currentTimeMillis() / 1000,
                fields = fieldList
        ))

        return SlackMessageRequest(attachments = attachmentRequest)
    }

    fun writeValueAsRequestBody(obj: SlackMessageRequest): RequestBody {
        return try {
            mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            RequestBody.create(MultipartBody.FORM, mapper.writeValueAsString(obj))
        }
        catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e.message)
        }
    }
}