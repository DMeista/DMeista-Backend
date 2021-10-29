package sinhee.kang.tutorial.domain.post.service.emoji

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse
import sinhee.kang.tutorial.domain.post.entity.emoji.Emoji
import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.repository.emoji.EmojiRepository
import sinhee.kang.tutorial.domain.post.repository.post.PostRepository
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException

@Service
class EmoJiServiceImpl(
    private val authService: AuthService,

    private val postRepository: PostRepository,
    private val emojiRepository: EmojiRepository
) : EmojiService {

    override fun getPostEmojiUserList(postId: Int): PostEmojiListResponse {
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        return PostEmojiListResponse(post)
    }

    override fun setEmoji(postId: Int, emojiStatus: EmojiStatus): EmojiResponse? {
        val user = authService.getCurrentUser()
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val emoji = emojiRepository.findByUserAndPost(user, post)
            ?.apply {
                if (status == emojiStatus)
                    emojiRepository.delete(this)
                else emojiRepository.save(update(emojiStatus))
            }
            ?: emojiRepository.save(Emoji(user, post, emojiStatus))

        return EmojiResponse(emoji)
    }
}
