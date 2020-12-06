package sinhee.kang.tutorial.domain.post.dto.response

class PostListResponse(
        var totalItems: Int,
        var totalPages: Int,
        var applicationResponses: MutableList<PostResponse>
)