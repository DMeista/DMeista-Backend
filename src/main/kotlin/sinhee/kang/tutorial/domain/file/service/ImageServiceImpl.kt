package sinhee.kang.tutorial.domain.file.service

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.file.domain.ImageFile
import sinhee.kang.tutorial.domain.file.domain.repository.ImageFileRepository
import sinhee.kang.tutorial.domain.file.exception.ImageNotFoundException
import sinhee.kang.tutorial.domain.post.domain.post.Post

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.*

@Service
class ImageServiceImpl(
        private val imageFileRepository: ImageFileRepository,

        @Value("\${upload.path}")
        private val imagePath: String
): ImageService {

    override fun getImage(imageName: String): ByteArray {
        val file = File(imagePath, imageName)
        if (!file.exists())
            throw ImageNotFoundException()
        val inputStream: InputStream = FileInputStream(file)

        return IOUtils.toByteArray(inputStream)
    }

    override fun saveImageFiles(post: Post, imageFiles: Array<MultipartFile>?) {
        imageFiles?.let {
            for (image in it) {
                val fileName = UUID.randomUUID().toString()
                image.transferTo(File(imagePath, fileName))

                imageFileRepository.save(ImageFile(
                    post = post,
                    fileName = fileName
                ))
            }
        }
    }

    override fun deleteImageFiles(post: Post, imageFiles: List<ImageFile>?) {
        imageFiles?.let {
            for (image in it) {
                Files.delete(File(imagePath, image.fileName).toPath())
            }
            imageFileRepository.deleteByPost(post)
        }
    }
}
