package sinhee.kang.tutorial.application.dto.response.post

import org.springframework.data.domain.Page
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.user.entity.user.User

data class PostListResponse(
    val totalItems: Int = 0,

    val totalPages: Int = 0,

    val applications: MutableList<PostPreviewResponse> = arrayListOf()
) {
    constructor(user: User?, posts: Page<Post>) : this (
        totalItems = posts.totalElements.toInt(),
        totalPages = posts.totalPages,
        applications = getPostsList(user, posts)
    )

    companion object {
        private fun getPostsList(user: User?, posts: Page<Post>): MutableList<PostPreviewResponse> =
            mutableListOf<PostPreviewResponse>().apply {
                posts.forEach { post: Post ->
                    add(PostPreviewResponse(user, post))
                }
            }
    }
}
