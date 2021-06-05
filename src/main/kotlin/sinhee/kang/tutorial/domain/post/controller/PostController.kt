package sinhee.kang.tutorial.domain.post.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostContentResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostListResponse
import sinhee.kang.tutorial.domain.post.service.emoji.EmojiService
import sinhee.kang.tutorial.domain.post.service.post.PostService

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
    private val emojiService: EmojiService
) {

    @GetMapping
    fun getAllHashTagPostList(page: Pageable,
                              @RequestParam(defaultValue = "") tags: String): PostListResponse =
        postService.getAllHashTagList(page, tags)

    @GetMapping("/{postId}")
    fun getPostContent(@PathVariable postId: Int): PostContentResponse =
        postService.getPostContent(postId)

    @PostMapping
    fun uploadPost(@RequestParam title: String,
                   @RequestParam content: String,
                   @RequestParam tags: List<String>?,
                   @RequestParam autoTag: Boolean?,
                   @RequestParam imageFiles: Array<MultipartFile>?): Int? =
        postService.uploadPost(title, content, tags, autoTag?: false, imageFiles)

    @PatchMapping("/{postId}")
    fun editPost(@PathVariable postId: Int,
                 @RequestParam(required = false) title: String?,
                 @RequestParam(required = false) content: String?,
                 @RequestParam(required = false) tags: List<String>?,
                 @RequestParam(required = false) imageFiles: Array<MultipartFile>?): Int? =
        postService.changePost(postId, title, content, tags, imageFiles)

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Int) =
        postService.deletePost(postId)

    @GetMapping("/{postId}/emoji")
    fun getEmojiUser(@PathVariable postId: Int): PostEmojiListResponse =
        emojiService.getPostEmojiUserList(postId)

    @PostMapping("/{postId}/emoji")
    fun emojiPost(@PathVariable postId: Int,
                  @RequestParam status: EmojiStatus): EmojiResponse? =
        emojiService.emojiService(postId, status)
}
