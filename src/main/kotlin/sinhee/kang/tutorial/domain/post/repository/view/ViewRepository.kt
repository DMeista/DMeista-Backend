package sinhee.kang.tutorial.domain.post.repository.view

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.entity.view.View
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface ViewRepository : CrudRepository<View, Int> {
    fun findByUserAndPost(user: User?, post: Post): View?

    fun findByPost(post: Post): MutableList<View>
}
