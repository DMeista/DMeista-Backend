package sinhee.kang.tutorial.infra.api.kakao.dto

data class AdultDetectionResponse(
    val rid: String,

    val result: Result
) {
    data class Result(
        val normal: Float,

        val soft: Float,

        val adult: Float
    )
}
