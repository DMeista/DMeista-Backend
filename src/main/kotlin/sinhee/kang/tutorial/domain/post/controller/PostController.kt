package sinhee.kang.tutorial.domain.post.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.post.service.post.PostService

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
    fun getAllHashTagList(page: Pageable, @RequestParam("tags") tags: String?): PostListResponse {
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
    fun uploadPost(@RequestParam title: String,
                   @RequestParam content: String,
                   @RequestParam tags: String?,
                   @RequestParam autoTag: Boolean,
                   @RequestParam imageFile: Array<MultipartFile>?): Int? {
        return postService.uploadPost(title, content, tags, autoTag, imageFile)
    }

    @PatchMapping("/{postId}")
    fun editPost(@PathVariable postId: Int,
                 @RequestParam title: String,
                 @RequestParam content: String,
                 @RequestParam tags: String?,
                 @RequestParam imageFile: Array<MultipartFile>?): Int? {
        return postService.changePost(postId, title, content, tags, imageFile)
    }

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Int) {
        postService.deletePost(postId)
    }

}