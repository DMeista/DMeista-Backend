package sinhee.kang.tutorial.domain.post.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.dto.request.PostRequest
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.post.service.post.PostService
import javax.validation.Valid

@RestController
@RequestMapping("/posts")
class PostController(
        private var postService: PostService
) {

    @GetMapping
    fun getAllPostList(page: Pageable): PostListResponse {
        return postService.getAllPostList(page)
    }

    @GetMapping("/")
    fun getAllHashTagList(page: Pageable, @RequestParam("tags", defaultValue = "") tags: String?): PostListResponse {
        return postService.getAllHashTagList(page, tags)
    }

    @GetMapping("/hit")
    fun getHitPosts(page: Pageable): PostListResponse {
        return postService.getHitPost(page)
    }

    @GetMapping("/{postId}")
    fun getPostContent(@PathVariable postId: Int): PostContentResponse {
        return postService.getPostContent(postId)
    }

    @PostMapping
    fun uploadPost(@Valid @RequestBody postRequest: PostRequest,
                   imageFile: Array<MultipartFile>?): Int? {
        return postService.uploadPost(
                postRequest.title,
                postRequest.content,
                postRequest.tags,
                imageFile
        )
    }

    @PatchMapping("/{postId}")
    fun editPost(@PathVariable postId: Int,
                 @Valid @RequestBody postRequest: PostRequest,
                 imageFile: Array<MultipartFile>?): Int? {
        return postService.changePost(
                postId,
                postRequest.title,
                postRequest.content,
                postRequest.tags,
                imageFile
        )
    }

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Int) {
        postService.deletePost(postId)
    }

}