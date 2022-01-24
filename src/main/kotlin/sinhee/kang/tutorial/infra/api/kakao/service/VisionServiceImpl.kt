package sinhee.kang.tutorial.infra.api.kakao.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.AccessDeniedException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.infra.api.kakao.VisionApi
import sinhee.kang.tutorial.infra.api.kakao.dto.AdultDetectionResponse
import sinhee.kang.tutorial.infra.api.kakao.dto.MultiTagResponse

@Component
class VisionServiceImpl(
    @Value("\${kakao.rest.api.key}")
    private val authorizationKey: String
) : VisionService {

    private val connection: VisionApi = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/vision/")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(VisionApi::class.java)

    override fun multiTaggingImage(imageFile: MultipartFile): List<String> {
        val formData: MultipartBody.Part = imageFile
            .createRequestBody()
            .createFormData(imageFile.name)

        return sendMultiTaggingRequest(formData).body()
            ?.result?.label_kr
            ?: listOf()
    }

    override fun detectAdultImage(imageFile: MultipartFile) {
        val formData: MultipartBody.Part = imageFile
            .createRequestBody()
            .createFormData(imageFile.name)

        sendAdultDetectionRequest(formData).body()
            ?.run {
                if (result.normal < result.adult)
                    throw AccessDeniedException()
            }
            ?: throw BadRequestException()
    }

    private fun MultipartFile.createRequestBody(): RequestBody =
        RequestBody.create(
            MediaType.parse("image/*"),
            bytes
        )

    private fun RequestBody.createFormData(fileName: String): MultipartBody.Part =
        MultipartBody.Part.createFormData("image", fileName, this)

    private fun sendMultiTaggingRequest(imageFile: MultipartBody.Part): Response<MultiTagResponse> =
        connection.multiTaggingImage(
            token = "KakaoAK $authorizationKey",
            imageFile = imageFile
        ).execute()

    private fun sendAdultDetectionRequest(imageFile: MultipartBody.Part): Response<AdultDetectionResponse> =
        connection.detectAdultImage(
            token = "KakaoAK $authorizationKey",
            imageFile = imageFile
        ).execute()
}
