package sinhee.kang.tutorial.post

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TokenType
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class PostApiTest: ApiTest() {
    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var userRepository: UserRepository

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
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @Throws
    fun uploadPostTest() {
        val post = generatePost(post("/posts"), token = accessToken)
        postRepository.findById(post)
            .orElseThrow { throw Exception() }
            .let { assert(post == it.postId) }
    }


    @Test
    @Throws
    fun editPostTest() {
        val post = generatePost(post("/posts"), token = accessToken)
        generatePost(patch("/posts/$post"), title = "new title", token = accessToken)
        postRepository.findById(post)
            .orElseThrow { Exception() }
            .let { assert(it.title == "new title") }
    }


    @Test
    @Throws
    fun deletePostTest() {
        val post = generatePost(post("/posts"), token = accessToken)
        mvc.perform(
            delete("/posts/$post")
                .header("Authorization", accessToken)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
    }
}
