package sinhee.kang.tutorial.infra.api.kakao

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import sinhee.kang.tutorial.infra.api.kakao.adult.dto.AdultResponse
import sinhee.kang.tutorial.infra.api.kakao.vision.dto.VisionResponse

interface KakaoApi {
    @Multipart
    @POST("/multitag/generate")
    fun generateMultiTagFromImage(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part
    ): Call<VisionResponse>

    @Multipart
    @POST("/adult/detect")
    fun detectAdultImage(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part
    ): Call<AdultResponse>
}
