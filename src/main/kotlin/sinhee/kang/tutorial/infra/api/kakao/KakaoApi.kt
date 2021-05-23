package sinhee.kang.tutorial.infra.api.kakao

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import sinhee.kang.tutorial.infra.api.kakao.vision.dto.VisionResponse

interface KakaoApi {
    @Multipart
    @POST("/v2/vision/multitag/generate")
    fun generateTagFromImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<VisionResponse>
}
