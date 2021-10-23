package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.dto.request.ChangePostRequest
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.post.dto.request.PostRequest

interface PostService {
    fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse

    fun getPostContent(postId: Int): PostContentResponse

    fun uploadPost(postRequest: PostRequest): Int

    fun changePost(changePostRequest: ChangePostRequest): Int

    fun deletePost(postId: Int)
}
