package sinhee.kang.tutorial.domain.post.dto.response

import org.springframework.data.domain.Page
import sinhee.kang.tutorial.domain.post.entity.post.Post

data class PostListResponse (
    val totalItems: Int = 0,

    val totalPages: Int = 0,

    val applications: MutableList<PostPreviewResponse> = arrayListOf()
) {
    constructor(posts: Page<Post>, applications: MutableList<PostPreviewResponse>): this (
        totalItems = posts.totalElements.toInt(),
        totalPages = posts.totalPages,
        applications = applications
    )
}
