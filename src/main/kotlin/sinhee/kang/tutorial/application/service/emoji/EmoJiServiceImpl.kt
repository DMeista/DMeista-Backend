package sinhee.kang.tutorial.application.service.emoji

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.application.dto.response.emoji.EmojiResponse
import sinhee.kang.tutorial.application.dto.response.emoji.PostEmojiListResponse
import sinhee.kang.tutorial.domain.post.entity.Emoji
import sinhee.kang.tutorial.domain.post.entity.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.repository.PostRepository
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.UnAuthorizedException

@Service
class EmoJiServiceImpl(
    private val requestAuthScope: RequestAuthScope,

    private val postRepository: PostRepository,
    private val emojiRepository: EmojiRepository
) : EmojiService {

    override fun getPostEmojiUserList(postId: Int): PostEmojiListResponse {
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        return PostEmojiListResponse(post)
    }

    override fun setEmoji(postId: Int, emojiStatus: EmojiStatus): EmojiResponse? {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val emoji = emojiRepository.findByUserAndPost(currentUser, post)
            ?.apply {
                if (status == emojiStatus)
                    emojiRepository.delete(this)
                else emojiRepository.save(update(emojiStatus))
            }
            ?: emojiRepository.save(Emoji(currentUser, post, emojiStatus))

        return EmojiResponse(emoji)
    }
}
