package sinhee.kang.tutorial.infra.api.kakao.vision.service

import org.springframework.web.multipart.MultipartFile

interface MultiTagService {
    fun generateMultiTag(imageFile: MultipartFile): List<String>
}
