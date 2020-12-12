package sinhee.kang.tutorial.domain.post.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.post.service.emoji.EmojiService
import sinhee.kang.tutorial.domain.post.service.post.PostService
import sinhee.kang.tutorial.domain.user.domain.user.User

@RestController
@RequestMapping("/posts")
class PostController(
        private var postService: PostService,
        private var emojiService: EmojiService
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

    @PostMapping("/{postId}/emoji")
    fun emojiPost(@PathVariable postId: Int, @RequestParam status: EmojiStatus): EmojiResponse? {
        return emojiService.emojiService(postId, status)
    }

    @GetMapping("/{postId}/emoji")
    fun getLikeUser(@PathVariable postId: Int): Any {
        return emojiService.getPostEmojiUserList(postId)
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