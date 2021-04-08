package sinhee.kang.tutorial.infra.api.vision

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.infra.api.vision.dto.VisionResponse

@Service
class VisionApi(
    @Value("\${kakao.rest.api.url}")
    private val url: String,
    @Value("\${kakao.prefix}")
    private val prefix: String,
    @Value("\${kakao.rest.api.key}")
    private val key: String
) {

    fun getVisionApi(imageFile: MultipartFile): List<String> {
        val client = OkHttpClient()
        val mapper = ObjectMapper()

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                imageFile.originalFilename,
                RequestBody.create(MediaType.parse("image/*"), imageFile.bytes)
            )
            .build()

        val request: Request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "$prefix $key")
                .addHeader("Content-Type", "multipart/form-data")
                .post(requestBody)
                .build()
        val response = client.newCall(request).execute().body()!!.string()

        return mapper.readValue<VisionResponse>(response).result.label_kr
    }
}