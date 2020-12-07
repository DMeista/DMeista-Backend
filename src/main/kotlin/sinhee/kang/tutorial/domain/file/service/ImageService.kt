package sinhee.kang.tutorial.domain.file.service

interface ImageService {
    fun getImage(imageName: String): ByteArray
}
