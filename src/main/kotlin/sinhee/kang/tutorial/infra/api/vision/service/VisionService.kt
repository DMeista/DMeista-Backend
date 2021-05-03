package sinhee.kang.tutorial.infra.api.vision.service

import org.springframework.web.multipart.MultipartFile

interface VisionService {
    fun generateTagFromImage(imageFile: MultipartFile): List<String>
}
