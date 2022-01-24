package sinhee.kang.tutorial.domain.image.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.image.entity.ImageFile
import sinhee.kang.tutorial.domain.post.entity.Post

@Repository
interface ImageFileRepository : CrudRepository<ImageFile, Int> {
    fun deleteByPost(post: Post)
}
