package sinhee.kang.tutorial.emoji

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.TestLib
import sinhee.kang.tutorial.domain.post.domain.emoji.Emoji
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.dto.response.PostEmojiListResponse

@Suppress("NonAsciiCharacters")
class GetEmojiListTest: TestLib() {

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
    fun `포스트 이모지 리스트`() {
        val post = postRepository.save(Post(
            user = user
        ))
        emojiRepository.save(Emoji(
            post = post,
            user = user,
            status = EmojiStatus.FUN
        ))

        val response = requestBody(get("/posts/${post.postId}/emoji"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString

        val emojiListResponse = mappingResponse(response, PostEmojiListResponse::class.java) as PostEmojiListResponse

        assert(emojiListResponse.applicationResponses[0].emojiStatus == EmojiStatus.FUN)
    }

    @Test
    fun `포스트를 찾을 수 없는 경우`() {
        requestBody(get("/posts/0/emoji"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
