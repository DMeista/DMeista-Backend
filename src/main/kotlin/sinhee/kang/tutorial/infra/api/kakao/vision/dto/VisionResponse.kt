package sinhee.kang.tutorial.infra.api.kakao.vision.dto

data class VisionResponse (
    val rid: String,

    val result: LabelResponse
) {
    data class LabelResponse(
        val label_kr: List<String>,

        val label: List<String>
    )
}
