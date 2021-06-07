package sinhee.kang.tutorial.emoji

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse

@Suppress("NonAsciiCharacters")
class AddEmojiTest: TestLib() {

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
    fun `이모지 추가`() {
        val postId = postRepository.save(Post(
            user = user
        )).postId
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
        .apply {  add("status", "${EmojiStatus.FUN}") }

        val response = requestParams(post("/posts/$postId/emoji"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
        val emojiResponse = mappingResponse(response, EmojiResponse::class.java) as EmojiResponse

        assert(emojiResponse.emojiStatus == EmojiStatus.FUN)
    }

    @Test
    fun `포스트를 찾을 수 없는 경우`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {  add("status", "${EmojiStatus.SAD}") }

        requestParams(post("/posts/0/emoji"), request, currentUserToken)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `사용자 인증이 확인되지 않음`() {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
            .apply {  add("status", "${EmojiStatus.SAD}") }

        requestParams(post("/posts/0/emoji"), request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
