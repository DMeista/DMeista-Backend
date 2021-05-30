package sinhee.kang.tutorial.domain.post.dto.response

data class PostListResponse (
    val totalItems: Int = 0,

    val totalPages: Int = 0,

    val applicationResponses: MutableList<PostResponse> = arrayListOf()
)
