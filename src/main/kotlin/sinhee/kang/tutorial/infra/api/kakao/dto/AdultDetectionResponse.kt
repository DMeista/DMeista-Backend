package sinhee.kang.tutorial.infra.api.kakao.dto

data class AdultDetectionResponse (
    val normal: Float,

    val soft: Float,

    val adult: Float
)
