package sinhee.kang.tutorial.domain.post.dto.response

class PostListResponse(
        var totalItems: Int = 0,
        var totalPages: Int = 0,
        var applicationResponses: MutableList<PostResponse> = arrayListOf()
)