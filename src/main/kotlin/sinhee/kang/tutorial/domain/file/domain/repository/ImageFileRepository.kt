package sinhee.kang.tutorial.domain.file.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.file.domain.ImageFile

@Repository
interface ImageFileRepository: CrudRepository<ImageFile, Int> {
}