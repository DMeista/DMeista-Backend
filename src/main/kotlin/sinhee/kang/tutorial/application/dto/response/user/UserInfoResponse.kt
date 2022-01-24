package sinhee.kang.tutorial.application.dto.response.user

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.application.dto.response.post.PostPreviewResponse
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime

data class UserInfoResponse(
    val username: String = "",

    val email: String = "",

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime? = null,

    val postList: MutableList<PostPreviewResponse> = mutableListOf()
) {
    constructor(user: User) : this (
        username = user.nickname,
        email = user.email,
        createdAt = user.createdAt,
        postList = getPostsList(user)
    )

    companion object {
        private fun getPostsList(user: User): MutableList<PostPreviewResponse> =
            mutableListOf<PostPreviewResponse>().apply {
                user.postList.forEach { post: Post ->
                    add(PostPreviewResponse(user, post))
                }
            }
    }
}
