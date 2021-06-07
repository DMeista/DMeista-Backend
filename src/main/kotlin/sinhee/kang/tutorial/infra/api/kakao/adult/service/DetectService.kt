package sinhee.kang.tutorial.infra.api.kakao.adult.service

import org.springframework.web.multipart.MultipartFile

interface DetectService {
    fun filterAdultImage(imageFile: MultipartFile)
}
