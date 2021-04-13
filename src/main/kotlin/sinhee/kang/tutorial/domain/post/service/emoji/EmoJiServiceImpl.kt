package sinhee.kang.tutorial.domain.post.service.emoji

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse
import sinhee.kang.tutorial.domain.post.exception.ApplicationNotFoundException

@Service
class EmoJiServiceImpl(
        private val authService: AuthService,

        private val postRepository: PostRepository,
        private val emojiRepository: EmojiRepository
): EmojiService {
    override fun emojiService(postId: Int, status: EmojiStatus): EmojiResponse? {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }

        lateinit var response: EmojiResponse

        emojiRepository.findByUserAndPostOrStatus(user, post, status)
                ?.let { emoji ->
                    if (emoji.status == status) {
                        emojiRepository.delete(emoji)
                    }
                    else if (emoji.status != status) {
                        emoji.status = status
                        emojiRepository.save(emoji)
                        response = EmojiResponse(
                                username = user.nickname,
                                postId = post.postId,
                                emojiStatus = emoji.status
                        )
                    }
                }
                ?: run {
                    val emoji = emojiRepository.save(Emoji(user = user, post = post, status = status))
                    response = EmojiResponse(
                        username = user.nickname,
                        postId = post.postId,
                        emojiStatus = emoji.status
                    )
                }
        return response
    }

    override fun getPostEmojiUserList(postId: Int): PostEmojiListResponse {
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        val emojiResponse: MutableList<EmojiResponse> = ArrayList()
        for (emoji in post.emojiList) {
            emojiResponse.add(EmojiResponse(
                    username = emoji.user.nickname,
                    postId = emoji.post.postId,
                    emojiStatus = emoji.status
            ))
        }
        return PostEmojiListResponse(
                totalEmoji = post.emojiList.count(),
                applicationResponses = emojiResponse
        )
    }
}
