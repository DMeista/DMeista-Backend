package sinhee.kang.tutorial.application.service.image

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.ImageNotFoundException
import sinhee.kang.tutorial.domain.image.entity.ImageFile
import sinhee.kang.tutorial.domain.image.repository.ImageFileRepository
import sinhee.kang.tutorial.domain.post.entity.Post
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.util.*
import javax.imageio.ImageIO

@Service
class ImageServiceImpl(
    @Value("\${upload.path}")
    private val uploadPath: String,

    private val imageFileRepository: ImageFileRepository
) : ImageService {

    override fun getImageFile(imageName: String): ByteArray {
        val file = File(uploadPath, imageName)

        if (!file.exists() || !file.isValidImage())
            throw ImageNotFoundException()

        return IOUtils.toByteArray(FileInputStream(file))
    }

    override fun saveImageFiles(post: Post, imageFiles: List<MultipartFile>) {
        for (image in imageFiles) {
            val fileName: String = generateUniqueUUID()
            image.transferTo(File(uploadPath, fileName))

            imageFileRepository.save(
                ImageFile(post = post, fileName = fileName)
            )
        }
    }

    override fun removeImageFiles(post: Post) {
        for (image in post.imageFileList) {
            Files.delete(File(uploadPath, image.fileName).toPath())
        }
        imageFileRepository.deleteByPost(post)
    }

    private fun generateUniqueUUID(): String {
        var fileName: String
        do {
            fileName = UUID.randomUUID().toString()
        } while (fileName.isExistImage())

        return fileName
    }

    private fun String.isExistImage(): Boolean =
        File(uploadPath, this).exists()

    private fun File.isValidImage(): Boolean =
        try { ImageIO.read(this) != null } catch (e: IOException) { false }
}
