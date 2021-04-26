package sinhee.kang.tutorial.domain.file.service

import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.file.domain.ImageFile
import sinhee.kang.tutorial.domain.post.domain.post.Post

interface ImageService {
    fun getImage(imageName: String): ByteArray
    fun saveImageFiles(post: Post, imageFiles: Array<MultipartFile>?)
    fun deleteImageFiles(post: Post, imageFiles: List<ImageFile>?)
}
