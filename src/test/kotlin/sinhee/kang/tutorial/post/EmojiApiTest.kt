package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TokenType
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class EmojiApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var emojiRepository: EmojiRepository

    private val user: User = User(
        nickname = "user",
        email = "rkdtlsgml40@dsm.hs.kr",
        password = passwordEncoder.encode("1234")
    )

    private lateinit var accessToken: String

    @BeforeEach
    fun setup() {
        userRepository.save(user)
        accessToken = "Bearer ${getToken(TokenType.ACCESS, user.email, "1234")}"
    }

    @AfterEach
    fun clean() {
        emojiRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }


    @Test
    @Throws
    fun addEmojiTest() {
        val postId = generatePost(token = accessToken)
        val emoji = requestEmoji(postId, EmojiStatus.LIKE)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.LIKE)
    }


    @Test
    @Throws
    fun changeEmojiTest() {
        val postId = generatePost(token = accessToken)
        requestEmoji(postId, EmojiStatus.LIKE)
        requestEmoji(postId, EmojiStatus.NICE)
        val emoji = requestEmoji(postId, EmojiStatus.FUN)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.FUN)
    }


    @Test
    @Throws
    fun removeEmojiTest() {
        val postId = generatePost(token = accessToken)
        requestEmoji(postId, EmojiStatus.SAD)
        requestEmoji(postId, EmojiStatus.SAD)
    }


    private fun requestEmoji(postId: Int, status: EmojiStatus): String =
        mvc.perform(
            post("/posts/$postId/emoji")
                .header("Authorization", accessToken)
                .param("status", "$status"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
}
