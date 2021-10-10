package sinhee.kang.tutorial.infra.api.kakao.adult.service

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
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.AccessDeniedException
import sinhee.kang.tutorial.global.exception.exceptions.badRequest.BadRequestException
import sinhee.kang.tutorial.infra.api.kakao.KakaoApi
import sinhee.kang.tutorial.infra.api.kakao.adult.dto.AdultResponse

@Component
class DetectServiceImpl(
    @Value("\${kakao.rest.api.key}")
    private val authorizationKey: String
): DetectService {
    private val connection = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/vision/")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(KakaoApi::class.java)

    override fun filterAdultImage(imageFile: MultipartFile) {
        val formData: MultipartBody.Part = imageFile
            .createRequestBody()
            .createFormData(imageFile.name)

        val adultResponse = sendRequest(formData).body()
            ?: throw BadRequestException()

        if (adultResponse.normal < adultResponse.adult)
            throw AccessDeniedException()
    }

    private fun MultipartFile.createRequestBody(): RequestBody =
        RequestBody.create(
            MediaType.parse("image/*"),
            this.bytes
        )

    private fun RequestBody.createFormData(fileName: String): MultipartBody.Part =
        MultipartBody.Part.createFormData("image", fileName, this)

    private fun sendRequest(imageFile: MultipartBody.Part): Response<AdultResponse> =
        connection.detectAdultImage(
            token = "KakaoAK $authorizationKey",
            imageFile = imageFile
        ).execute()
}
