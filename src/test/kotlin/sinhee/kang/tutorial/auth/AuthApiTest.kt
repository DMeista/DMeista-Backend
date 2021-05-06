package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.TokenType
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class AuthApiTest: ApiTest() {
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
       userRepository.deleteAll()
    }

    @Test
    fun signInTest() {
        requestBody(post("/auth"), SignInRequest(user.email, "1234"))
        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }
}
