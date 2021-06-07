package sinhee.kang.tutorial.emoji

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.post.Post

@Suppress("NonAsciiCharacters")
class RemoveEmojiTest: TestLib() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        currentUserToken = getAccessToken(signInRequest)
    }

    @AfterEach
    fun clean() {
        emojiRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `이모지 삭제`() {
        val post = postRepository.save(Post(
            user = user
        ))
        emojiRepository.save(Emoji(
            post = post,
            user = user,
            status = EmojiStatus.NICE
        ))

        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("status", "${EmojiStatus.NICE}") }

        requestParams(MockMvcRequestBuilders.post("/posts/${post.postId}/emoji"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `포스트를 찾을 수 없는 경우`() {
        requestBody(MockMvcRequestBuilders.get("/posts/0/emoji"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val post = postRepository.save(Post(
            user = user
        ))
        emojiRepository.save(Emoji(
            post = post,
            user = user,
            status = EmojiStatus.SAD
        ))

        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {  add("status", "${EmojiStatus.SAD}") }

        requestParams(MockMvcRequestBuilders.post("/posts/0/emoji"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
