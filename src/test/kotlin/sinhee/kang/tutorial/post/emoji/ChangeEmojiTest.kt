package sinhee.kang.tutorial.post.emoji

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.TestProperties
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.post.entity.emoji.Emoji
import sinhee.kang.tutorial.domain.post.entity.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.entity.post.Post

@Suppress("NonAsciiCharacters")
class ChangeEmojiTest : TestProperties() {

    private val testPath = "/posts"

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
    fun `이모지 변경 - Ok`() {
        val post = postRepository.save(Post(user = user))
        emojiRepository.save(
            Emoji(
                post = post,
                user = user,
                status = EmojiStatus.NICE
            )
        )

        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("status", "${EmojiStatus.FUN}") }

        val response = requestParams(post("$testPath/${post.postId}/emoji"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val emojiResponse = mappingResponse(response, EmojiResponse::class.java) as EmojiResponse

        assert(emojiResponse.emojiStatus == EmojiStatus.FUN)
    }

    @Test
    fun `포스트를 찾을 수 없는 경우 - NotFound`() {
        requestBody(MockMvcRequestBuilders.get("$testPath/0/emoji"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음 - Unauthorized`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply { add("status", "${EmojiStatus.SAD}") }

        requestParams(post("$testPath/0/emoji"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
