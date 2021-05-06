package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class PostApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var userRepository: UserRepository

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
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun uploadPostTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        postRepository.findById(postId)
            .orElseThrow { throw Exception() }
            .let { assert(postId == it.postId) }
    }

    @Test
    fun editPostTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        generatePost(patch("/posts/$postId"), title = "new title", cookie = cookie)
        postRepository.findById(postId)
            .orElseThrow { Exception() }
            .let { assert(it.title == "new title") }
    }

    @Test
    fun deletePostTest() {
        val cookie = login(SignInRequest(user.email, "1234"))
        val postId = generatePost(cookie = cookie)
        mvc.perform(delete("/posts/$postId").cookie(cookie))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
