package sinhee.kang.tutorial.domain.file.service

import org.apache.commons.io.IOUtils
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
        private val imageFileRepository: ImageFileRepository
): ImageService {

    val imagePath = "/image/"

    override fun getImage(imageName: String): ByteArray {
        val file = File(imagePath, imageName)
        if (!file.exists())
            throw ImageNotFoundException()
        val inputStream: InputStream = FileInputStream(file)

        return IOUtils.toByteArray(inputStream)
    }

    override fun saveImageFile(post: Post, imageFile: Array<MultipartFile>?) {
        imageFile?.let {
            for (img in it) {
                val fileName = UUID.randomUUID().toString()
                img.transferTo(File(imagePath, fileName))

                imageFileRepository.save(ImageFile(
                    post = post,
                    fileName = fileName
                ))
            }
        }
    }

    override fun deleteImageFile(post: Post, imageFile: List<ImageFile>?) {
        imageFile?.let {
            for (image in it) {
                Files.delete(File(imagePath, image.fileName).toPath())
            }
            imageFileRepository.deleteByPost(post)
        }
    }
}