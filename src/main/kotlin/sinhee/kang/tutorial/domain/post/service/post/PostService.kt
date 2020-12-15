package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.user.domain.user.User

interface PostService {
    fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse
    fun getHitPostList(pageable: Pageable): PostListResponse

    fun getPostContent(postId: Int): PostContentResponse
    fun uploadPost(title: String, content: String, tags: String?, autoTags: Boolean, imageFile: Array<MultipartFile>?): Int?
    fun changePost(postId: Int, title: String, content: String, tags: String?, image: Array<MultipartFile>?): Int?
    fun deletePost(postId: Int)
}