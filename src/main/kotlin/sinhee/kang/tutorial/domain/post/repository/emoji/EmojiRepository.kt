package sinhee.kang.tutorial.domain.post.repository.emoji

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.emoji.Emoji
import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface EmojiRepository: CrudRepository<Emoji, Int> {
    fun findByUserAndPostAndStatus(user: User, post: Post, status: EmojiStatus): Emoji?
}
