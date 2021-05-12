package sinhee.kang.tutorial.infra.api.kakao.service

import org.springframework.web.multipart.MultipartFile

interface VisionService {
    fun generateTagFromImage(imageFile: MultipartFile): List<String>
}
