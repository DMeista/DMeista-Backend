package sinhee.kang.tutorial.domain.post.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.Emoji
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface EmojiRepository : CrudRepository<Emoji, Int> {
    fun findByUserAndPost(user: User, post: Post): Emoji?
}
