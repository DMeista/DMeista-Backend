package sinhee.kang.tutorial.infra.api.kakao

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import sinhee.kang.tutorial.infra.api.kakao.dto.AdultDetectionResponse
import sinhee.kang.tutorial.infra.api.kakao.dto.MultiTagResponse

interface VisionApi {
    @Multipart
    @POST("/multitag/generate")
    fun multiTaggingImage(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part
    ): Call<MultiTagResponse>

    @Multipart
    @POST("/adult/detect")
    fun detectAdultImage(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part
    ): Call<AdultDetectionResponse>
}
