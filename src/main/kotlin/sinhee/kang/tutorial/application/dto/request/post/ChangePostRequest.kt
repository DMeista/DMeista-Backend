package sinhee.kang.tutorial.application.dto.request.post

import org.springframework.web.multipart.MultipartFile

data class ChangePostRequest(
    val postId: Int,

    val title: String?,

    val content: String?,

    val tags: List<String>?,

    val autoTags: Boolean?,

    val imageFiles: List<MultipartFile>?
)
