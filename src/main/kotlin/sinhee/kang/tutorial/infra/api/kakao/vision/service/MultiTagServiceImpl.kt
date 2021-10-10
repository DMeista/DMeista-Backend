package sinhee.kang.tutorial.infra.api.kakao.vision.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import sinhee.kang.tutorial.infra.api.kakao.KakaoApi
import sinhee.kang.tutorial.infra.api.kakao.vision.dto.VisionResponse

@Component
class MultiTagServiceImpl(
    @Value("\${kakao.rest.api.key}")
    private val authorizationKey: String
): MultiTagService {
    private val connection = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/vision/")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(KakaoApi::class.java)

    override fun generateMultiTag(imageFile: MultipartFile): List<String> {
        val formData: MultipartBody.Part = imageFile
            .createRequestBody()
            .createFormData(imageFile.name)

        return sendRequest(formData).body()
            ?.result?.label_kr
            ?: listOf()
    }

    private fun MultipartFile.createRequestBody(): RequestBody =
        RequestBody.create(
            MediaType.parse("image/*"),
            this.bytes
        )

    private fun RequestBody.createFormData(fileName: String): MultipartBody.Part =
        MultipartBody.Part.createFormData("image", fileName, this)

    private fun sendRequest(imageFile: MultipartBody.Part): Response<VisionResponse> =
        connection.generateMultiTagFromImage(
            token = "KakaoAK $authorizationKey",
            imageFile = imageFile
        ).execute()
}
