package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.TestApis
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse

class EmojiTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
        postId = generatePost(token = currentUserToken)
    }

    @AfterEach
    fun clean() {
        emojiRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun addEmojiTest() {
        val emoji = requestEmoji(postId, EmojiStatus.LIKE, currentUserToken)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.LIKE)
    }

    @Test
    fun changeEmojiTest() {
        requestEmoji(postId, EmojiStatus.LIKE, currentUserToken)
        val emoji = requestEmoji(postId, EmojiStatus.FUN, currentUserToken)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.FUN)
    }

    @Test
    fun removeEmojiTest() {
        requestEmoji(postId, EmojiStatus.SAD, currentUserToken)
        requestEmoji(postId, EmojiStatus.SAD, currentUserToken)
    }

    private fun requestEmoji(postId: Int, status: EmojiStatus, token: String?): String =
        mvc.perform(
            post("/posts/$postId/emoji")
                .header("Authorization", token)
                .param("status", "$status"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
}
