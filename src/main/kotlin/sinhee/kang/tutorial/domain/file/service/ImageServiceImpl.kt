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
        @Value("\${image.upload.dir}")
        private var imageDirPath: String,

        private var imageFileRepository: ImageFileRepository
): ImageService {

    override fun getImage(imageName: String): ByteArray {
        val file = File(imageDirPath, imageName)
        if (!file.exists())
            throw ImageNotFoundException()
        val inputStream: InputStream = FileInputStream(file)

        return IOUtils.toByteArray(inputStream)
    }

    override fun saveImageFile(post: Post, imageFile: Array<MultipartFile>?) {
        imageFile?.let {
            for (file in imageFile) {
                val fileName = UUID.randomUUID().toString()
                imageFileRepository.save(ImageFile(
                        post = post,
                        fileName = fileName
                ))
                file.transferTo(File(imageDirPath, fileName))
            }
        }
    }

    override fun deleteImageFile(post: Post, imageFile: List<ImageFile>?) {
        imageFile?.let {
            for (image in imageFile) {
                Files.delete(File(imageDirPath, image.fileName).toPath())
            }
            imageFileRepository.deleteByPost(post)
        }
    }
}