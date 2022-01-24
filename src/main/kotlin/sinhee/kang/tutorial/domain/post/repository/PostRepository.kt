package sinhee.kang.tutorial.domain.post.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.Post

@Repository
interface PostRepository : CrudRepository<Post, Int> {
    fun findTop1ByPostIdAfterOrderByPostIdAsc(id: Int): Post?

    fun findTop1ByPostIdBeforeOrderByPostIdDesc(id: Int): Post?

    fun findByTagsContainsOrderByCreatedAtDesc(pageable: Pageable, tags: String?): Page<Post>?
}
