package sinhee.kang.tutorial.domain.image.service

import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.image.entity.ImageFile
import sinhee.kang.tutorial.domain.post.entity.post.Post

interface ImageService {
    fun getImageFile(imageName: String): ByteArray

    fun saveImageFiles(post: Post, imageFiles: List<MultipartFile>)

    fun removeImageFiles(post: Post)
}
