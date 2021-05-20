package sinhee.kang.tutorial.infra.api.kakao.service

import org.springframework.web.multipart.MultipartFile

interface VisionLabelService {
    fun generateTagFromImage(imageFile: MultipartFile): List<String>
}
