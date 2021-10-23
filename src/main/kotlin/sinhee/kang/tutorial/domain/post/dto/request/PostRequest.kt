package sinhee.kang.tutorial.domain.post.dto.request

import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.user.entity.user.User

data class PostRequest(
    val title: String,

    val content: String,

    val tags: List<String>?,

    val autoTags: Boolean,

    val imageFiles: List<MultipartFile>?
) {
    fun toEntity(user: User, tags: MutableSet<String>): Post =
        Post(
            user = user,
            title = title,
            content = content,
            tags = tags.joinToString { "#$it" }
        )
}
