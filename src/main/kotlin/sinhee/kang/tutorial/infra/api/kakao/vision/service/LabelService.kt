package sinhee.kang.tutorial.infra.api.kakao.vision.service

import org.springframework.web.multipart.MultipartFile

interface LabelService {
    fun generateTagFromImage(imageFile: MultipartFile): List<String>
}
