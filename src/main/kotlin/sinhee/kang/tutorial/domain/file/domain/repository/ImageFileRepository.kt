package sinhee.kang.tutorial.domain.file.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.file.domain.ImageFile
import sinhee.kang.tutorial.domain.post.domain.post.Post

@Repository
interface ImageFileRepository: CrudRepository<ImageFile, Int> {
    fun deleteByPost(post: Post)
    fun findByPostOrderByImageId(post: Post): List<ImageFile>?
}