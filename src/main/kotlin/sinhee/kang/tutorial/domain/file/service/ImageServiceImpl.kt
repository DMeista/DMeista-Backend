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
    val imageDirPath: String = "file:///home/resource/"

    override fun getImage(imageName: String): ByteArray {
        val file = File(imageDirPath, imageName)
        if (!file.exists())
            throw ImageNotFoundException()
        val inputStream: InputStream = FileInputStream(file)

        return IOUtils.toByteArray(inputStream)
    }

    override fun saveImageFile(post: Post, imageFiles: Array<MultipartFile>?) {
        imageFiles?.let {
            for (img in imageFiles) {
                val fileName = UUID.randomUUID().toString()
                img.transferTo(File(imageDirPath, fileName))

                imageFileRepository.save(ImageFile(
                    post = post,
                    fileName = fileName
                ))
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