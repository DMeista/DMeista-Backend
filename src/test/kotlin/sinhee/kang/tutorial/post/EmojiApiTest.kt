package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.post.domain.emoji.enums.EmojiStatus
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.dto.response.EmojiResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import javax.servlet.http.Cookie

class EmojiApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var emojiRepository: EmojiRepository

    private val user: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user",
        password = passwordEncoder.encode("1234")
    )

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        emojiRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun addEmojiTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        val emoji = requestEmoji(postId, EmojiStatus.LIKE, cookie)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.LIKE)
    }

    @Test
    fun changeEmojiTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        requestEmoji(postId, EmojiStatus.LIKE, cookie)
        requestEmoji(postId, EmojiStatus.NICE, cookie)
        val emoji = requestEmoji(postId, EmojiStatus.FUN, cookie)

        val response = mappingResponse(emoji, EmojiResponse::class.java) as EmojiResponse
        assert(response.emojiStatus == EmojiStatus.FUN)
    }

    @Test
    fun removeEmojiTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        requestEmoji(postId, EmojiStatus.SAD, cookie)
        requestEmoji(postId, EmojiStatus.SAD, cookie)
    }

    private fun requestEmoji(postId: Int, status: EmojiStatus, cookie: Cookie?): String =
        mvc.perform(
            post("/posts/$postId/emoji")
                .param("status", "$status")
                .cookie(cookie))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
}
