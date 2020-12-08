package sinhee.kang.tutorial.domain.file.service

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.file.exception.ImageNotFoundException

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@Service
class ImageServiceImpl(
        @Value("\${image.upload.dir}")
        private var imageDirPath: String
): ImageService {

    override fun getImage(imageName: String): ByteArray {
        val file = File(imageDirPath, imageName)
        if (!file.exists())
            throw ImageNotFoundException()
        val inputStream: InputStream = FileInputStream(file)

        return IOUtils.toByteArray(inputStream)
    }
}