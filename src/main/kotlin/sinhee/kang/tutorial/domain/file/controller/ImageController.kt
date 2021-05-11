package sinhee.kang.tutorial.domain.file.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sinhee.kang.tutorial.domain.file.service.ImageService

@RestController
@RequestMapping("/image")
class ImageController(
    private val imageService: ImageService
) {
    @GetMapping(value = ["/{imageName}"],
                produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
    fun getImages(@PathVariable imageName: String): ByteArray {
        return imageService.getImage(imageName)
    }
}
