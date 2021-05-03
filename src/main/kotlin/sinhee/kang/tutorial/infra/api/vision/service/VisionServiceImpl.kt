package sinhee.kang.tutorial.infra.api.vision.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import sinhee.kang.tutorial.infra.api.vision.KakaoApi

@Component
class VisionServiceImpl(
    @Value("\${kakao.rest.api.key}")
    private val authorizationKey: String
): VisionService {
    private val visionConnection = Retrofit
        .Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
        .create(KakaoApi::class.java)

    override fun generateTagFromImage(imageFile: MultipartFile): List<String> {
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile.bytes)
        val body: MultipartBody.Part = MultipartBody.Part
            .createFormData("image", imageFile.name, requestFile)

        return visionConnection
            .generateTagFromImage(token = "KakaoAK $authorizationKey", image = body)
            .execute().body()?.result?.label_kr!!
    }
}
