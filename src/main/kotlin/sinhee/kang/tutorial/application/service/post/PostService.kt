package sinhee.kang.tutorial.application.service.post

import org.springframework.data.domain.Pageable
import sinhee.kang.tutorial.application.dto.request.post.ChangePostRequest
import sinhee.kang.tutorial.application.dto.request.post.PostRequest
import sinhee.kang.tutorial.application.dto.response.post.PostContentResponse
import sinhee.kang.tutorial.application.dto.response.post.PostListResponse

interface PostService {
    fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse

    fun getPostContent(postId: Int): PostContentResponse

    fun generatePost(postRequest: PostRequest): Int

    fun changePost(changePostRequest: ChangePostRequest): Int

    fun removePost(postId: Int)
}
