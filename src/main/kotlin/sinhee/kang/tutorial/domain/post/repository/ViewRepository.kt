package sinhee.kang.tutorial.domain.post.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.post.entity.View
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface ViewRepository : CrudRepository<View, Int> {
    fun findByUserAndPost(user: User?, post: Post): View?

    fun findByPost(post: Post): MutableList<View>
}
