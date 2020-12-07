package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse

interface PostService {
    fun getAllPostList(pageable: Pageable): PostListResponse
    fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse
    fun getHitPost(pageable: Pageable): PostListResponse

    //TODO. CRUD
    fun getPostContent(postId: Int): PostContentResponse
    fun uploadPost(title: String, content: String, tags: String?, imageFile: Array<MultipartFile>?): Int?
    fun changePost(postId: Int, title: String, content: String, tags: String?, image: Array<MultipartFile>?): Int?
    fun deletePost(postId: Int)
}