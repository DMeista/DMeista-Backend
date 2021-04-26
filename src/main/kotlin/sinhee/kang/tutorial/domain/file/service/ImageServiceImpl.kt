package sinhee.kang.tutorial.domain.file.service

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.file.domain.ImageFile
import sinhee.kang.tutorial.domain.file.domain.repository.ImageFileRepository
import sinhee.kang.tutorial.domain.file.exception.ImageNotFoundException
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.global.security.exception.BadRequestException

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.util.*

@Service
class ImageServiceImpl(
    @Value("\${upload.path}")
    private val imagePath: String,

    private val imageFileRepository: ImageFileRepository
): ImageService {

    @Cacheable(value = ["cache"])
    override fun getImage(imageName: String): ByteArray {
        val file = File(imagePath, imageName)
        if (!file.exists()) throw ImageNotFoundException()

        return IOUtils.toByteArray(FileInputStream(file))
    }

    override fun saveImageFiles(post: Post, imageFiles: Array<MultipartFile>?) {
        if (imageFiles.isNullOrEmpty()) throw BadRequestException()

        for (image in imageFiles) {
            val fileName: String = generateUniqueUUID()
            image.transferTo(File(imagePath, fileName))

            imageFileRepository.save(ImageFile(
                post = post,
                fileName = fileName
            ))
        }
    }

    override fun deleteImageFiles(post: Post, imageFiles: List<ImageFile>?) {
        if (imageFiles.isNullOrEmpty()) throw ImageNotFoundException()

        for (image in imageFiles) {
            Files.delete(File(imagePath, image.fileName).toPath())
        }
        imageFileRepository.deleteByPost(post)
    }

    private fun generateUniqueUUID(): String {
        var fileName: String
        do {
            fileName = UUID.randomUUID().toString()
        } while (fileName.isExistFileName())

        return fileName
    }

    private fun String.isExistFileName(): Boolean {
        val file = File(imagePath, this)
        return file.exists()
    }
}
