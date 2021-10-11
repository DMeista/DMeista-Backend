package sinhee.kang.tutorial.infra.api.kakao.service

import org.springframework.web.multipart.MultipartFile

interface VisionService {
    fun multiTaggingImage(imageFile: MultipartFile): List<String>

    fun detectAdultImage(imageFile: MultipartFile)
}
