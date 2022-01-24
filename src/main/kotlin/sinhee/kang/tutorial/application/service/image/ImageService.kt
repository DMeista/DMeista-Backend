package sinhee.kang.tutorial.application.service.image

import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.post.entity.Post

interface ImageService {
    fun getImageFile(imageName: String): ByteArray

    fun saveImageFiles(post: Post, imageFiles: List<MultipartFile>)

    fun removeImageFiles(post: Post)
}
