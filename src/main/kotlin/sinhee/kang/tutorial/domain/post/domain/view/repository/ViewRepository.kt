package sinhee.kang.tutorial.domain.post.domain.view.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.view.View
import sinhee.kang.tutorial.domain.user.domain.user.User

@Repository
interface ViewRepository: CrudRepository<View, Int> {
    fun findByUserAndPost(user: User, post: Post): View?
    fun findByPost(post: Post): MutableList<View>
}