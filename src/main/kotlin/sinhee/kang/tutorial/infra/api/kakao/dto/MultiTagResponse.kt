package sinhee.kang.tutorial.infra.api.kakao.dto

data class MultiTagResponse(
    val rid: String,

    val result: Result
) {
    data class Result(
        val label_kr: List<String>,

        val label: List<String>
    )
}
