package sinhee.kang.tutorial.controller.post

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.application.dto.request.post.ChangePostRequest
import sinhee.kang.tutorial.application.dto.request.post.PostRequest
import sinhee.kang.tutorial.application.dto.response.emoji.EmojiResponse
import sinhee.kang.tutorial.application.dto.response.post.PostContentResponse
import sinhee.kang.tutorial.application.dto.response.emoji.PostEmojiListResponse
import sinhee.kang.tutorial.application.dto.response.post.PostListResponse
import sinhee.kang.tutorial.domain.post.entity.enums.EmojiStatus
import sinhee.kang.tutorial.application.service.emoji.EmojiService
import sinhee.kang.tutorial.application.service.post.PostService
import sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
    private val emojiService: EmojiService
) {

    @Authentication
    @GetMapping
    fun getAllHashTagPostList(page: Pageable, @RequestParam(defaultValue = "") tags: String): PostListResponse =
        postService.getAllHashTagList(page, tags)

    @Authentication
    @GetMapping("/{postId}")
    fun getPostContent(@PathVariable postId: Int): PostContentResponse =
        postService.getPostContent(postId)

    @Authentication
    @PostMapping
    fun uploadPost(
        @RequestParam title: String,
        @RequestParam content: String,
        @RequestParam(required = false) tags: List<String>?,
        @RequestParam(required = false) autoTag: Boolean?,
        @RequestParam(required = false) imageFiles: List<MultipartFile>?
    ): Int =
        postService.generatePost(PostRequest(title, content, tags, autoTag ?: false, imageFiles))

    @Authentication
    @PatchMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Int,
        @RequestParam title: String?,
        @RequestParam content: String?,
        @RequestParam tags: List<String>?,
        @RequestParam autoTag: Boolean?,
        @RequestParam imageFiles: List<MultipartFile>?
    ): Int? =
        postService.changePost(ChangePostRequest(postId, title, content, tags, autoTag, imageFiles))

    @Authentication
    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Int) =
        postService.removePost(postId)

    @Authentication
    @GetMapping("/{postId}/emoji")
    fun getPostEmojiUserList(@PathVariable postId: Int): PostEmojiListResponse =
        emojiService.getPostEmojiUserList(postId)

    @Authentication
    @PostMapping("/{postId}/emoji")
    fun setEmoji(
        @PathVariable postId: Int,
        @RequestParam status: EmojiStatus
    ): EmojiResponse? =
        emojiService.setEmoji(postId, status)
}
