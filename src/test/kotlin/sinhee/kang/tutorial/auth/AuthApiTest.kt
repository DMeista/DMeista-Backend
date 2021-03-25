package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

class AuthApiTest: ApiTest() {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val testMail = "rkdtlsgml50@naver.com"
    private val passwd = "1234"
    private val username = "user"


    @BeforeEach
    fun setup() {
        userRepository.save(User(
                email = testMail,
                password = passwordEncoder.encode(passwd),
                nickname = username
        ))
    }

    @AfterEach
    fun clean() {
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }
    }


    @Test
    @Throws
    fun signInTest() {
        signIn()
    }


    @Test
    @Throws
    fun refreshTokenTest() {
        requestMvc(put("/auth"), token = refreshKey())
    }


    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any? = null, token: String? = ""): String {
        return mvc.perform(
                method
                        .header("X-Refresh-Token", token)
                        .content(objectMapper
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                .writeValueAsString(obj))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    private fun signIn(): String {
        return requestMvc(post("/auth"), SignInRequest(testMail, passwd))
    }


    private fun refreshKey(): String {
        val content = signIn()
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse
        return response.refreshToken
    }


    private fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(obj, cls)
    }
}