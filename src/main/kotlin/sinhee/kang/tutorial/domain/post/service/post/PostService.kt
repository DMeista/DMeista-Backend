package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Pageable
import sinhee.kang.tutorial.domain.post.dto.request.ChangePostRequest
import sinhee.kang.tutorial.domain.post.dto.request.PostRequest
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse

interface PostService {
    fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse

    fun getPostContent(postId: Int): PostContentResponse

    fun generatePost(postRequest: PostRequest): Int

    fun changePost(changePostRequest: ChangePostRequest): Int

    fun removePost(postId: Int)
}
