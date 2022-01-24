package sinhee.kang.tutorial.application.dto.response.post

import com.fasterxml.jackson.annotation.JsonFormat
import sinhee.kang.tutorial.application.dto.response.comment.PostCommentsResponse
import sinhee.kang.tutorial.domain.post.entity.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.user.entity.user.User
import java.time.LocalDateTime

data class PostContentResponse(
    val title: String? = null,

    val author: String = "",

    val tags: String? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime? = null,

    val viewCount: Int = 0,

    val emojiCount: Int = 0,

    val emoji: EmojiStatus? = null,

    val content: String = "",

    val isMine: Boolean = false,

    val nextPostTitle: String? = null,

    val prevPostTitle: String? = null,

    val nextPostId: Int? = null,

    val prevPostId: Int? = null,

    val images: List<String> = arrayListOf(),

    val comments: List<PostCommentsResponse> = arrayListOf()
) {
    constructor(user: User?, post: Post, nextPost: Post?, prevPost: Post?) : this(
        title = post.title,
        content = post.content,
        author = post.user.nickname,
        tags = post.tags,
        viewCount = post.viewList.count(),
        emojiCount = post.emojiList.count(),
        emoji = post.emojiList
            .filter { emoji -> emoji.user == user }
            .map { it.status }.firstOrNull(),
        createdAt = post.createdAt,
        isMine = (post.user == user),

        nextPostTitle = nextPost?.title,
        prevPostTitle = prevPost?.title,

        nextPostId = nextPost?.postId,
        prevPostId = prevPost?.postId,

        images = post.imageFileList
            .map { it.fileName }.toList(),
        comments = getPostComments(user, post)
    )

    companion object {
        private fun getPostComments(user: User?, post: Post): MutableList<PostCommentsResponse> =
            mutableListOf<PostCommentsResponse>().apply {
                post.commentList.forEach { comment ->
                    add(PostCommentsResponse(user, comment))
                }
            }
    }
}
